package io.github.styxiner.complyx_api.regulations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegulationService {

    private final RegulationRepository regulationRepository;
    private final RegSectionRepository regSectionRepository;

    private static final String PDF_STORAGE_PATH = "uploads/regulations/";

    public RegulationService(RegulationRepository regulationRepository,
                             RegSectionRepository regSectionRepository) {
        this.regulationRepository = regulationRepository;
        this.regSectionRepository = regSectionRepository;
    }

    public Page<RegulationSummaryDTO> findAll(RegulationFilter filter, Pageable pageable) {
        Page<RegulationEntity> page = regulationRepository.findAll(RegulationSpecifications.build(filter), pageable);
        return page.map(new java.util.function.Function<RegulationEntity, RegulationSummaryDTO>() {
            @Override
            public RegulationSummaryDTO apply(RegulationEntity entity) {
                return toSummaryDTO(entity);
            }
        });
    }

    public RegulationDetailDTO findById(UUID regulationId) {
        RegulationEntity entity = regulationRepository.findById(regulationId)
            .orElseThrow(new java.util.function.Supplier<ResponseStatusException>() {
                @Override
                public ResponseStatusException get() {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la normativa");
                }
            });
        return toDetailDTO(entity);
    }

    public RegulationDetailDTO create(RegulationCreateDTO dto) {
        if (regulationRepository.findByName(dto.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una normativa con el mismo nombre");
        }
        RegulationEntity entity = new RegulationEntity(dto.getName());
        return toDetailDTO(regulationRepository.save(entity));
    }

    public RegulationDetailDTO update(UUID regulationId, RegulationUpdateDTO dto) {
        RegulationEntity entity = regulationRepository.findById(regulationId)
            .orElseThrow(new java.util.function.Supplier<ResponseStatusException>() {
                @Override
                public ResponseStatusException get() {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la normativa");
                }
            });
        if (dto.getName() != null) entity.setName(dto.getName());
        return toDetailDTO(regulationRepository.save(entity));
    }

    public void delete(UUID regulationId) {
        if (!regulationRepository.existsById(regulationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la normativa");
        }
        regulationRepository.deleteById(regulationId);
    }

    public String storePdf(UUID regulationId, MultipartFile pdf) {
        RegulationEntity entity = regulationRepository.findById(regulationId)
            .orElseThrow(new java.util.function.Supplier<ResponseStatusException>() {
                @Override
                public ResponseStatusException get() {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la normativa");
                }
            });
        try {
            Path dir = Paths.get(PDF_STORAGE_PATH);
            Files.createDirectories(dir);
            String filename = regulationId + "_" + pdf.getOriginalFilename();
            Path destination = dir.resolve(filename);
            pdf.transferTo(destination);
            entity.setPdfPath(destination.toString());
            regulationRepository.save(entity);
            return destination.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se ha podido almacenar el PDF");
        }
    }

    public RegulationDetailDTO addSection(UUID regulationId, RegSectionCreateDTO dto) {
        RegulationEntity entity = regulationRepository.findById(regulationId)
            .orElseThrow(new java.util.function.Supplier<ResponseStatusException>() {
                @Override
                public ResponseStatusException get() {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la normativa");
                }
            });
        RegulationSectionEntity section = new RegulationSectionEntity(dto.getTitle(), entity);
        regSectionRepository.save(section);
        RegulationEntity updated = regulationRepository.findById(regulationId).get();
        return toDetailDTO(updated);
    }

    private RegulationSummaryDTO toSummaryDTO(RegulationEntity entity) {
        RegulationSummaryDTO dto = new RegulationSummaryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddedDate(entity.getAddedDate());
        return dto;
    }

    private RegulationDetailDTO toDetailDTO(RegulationEntity entity) {
        RegulationDetailDTO dto = new RegulationDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPdfPath(entity.getPdfPath());

        List<RegSectionDTO> sectionDTOs = new ArrayList<>();
        if (entity.getSections() != null) {
            for (RegulationSectionEntity section : entity.getSections()) {
                sectionDTOs.add(toSectionDTO(section));
            }
        }
        dto.setSections(sectionDTOs);
        return dto;
    }

    private RegSectionDTO toSectionDTO(RegulationSectionEntity entity) {
        RegSectionDTO dto = new RegSectionDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }
}