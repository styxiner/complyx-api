package io.github.styxiner.complyx_api.regulations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegulationService {

    private final RegulationRepository regulationRepository;
    private final RegSectionRepository regSectionRepository;

    @Value("${complyx.regulations.upload-path:/var/lib/complyx/regulations}")
    private String pdfStoragePath;

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

    @Transactional
    public String storePdf(UUID regulationId, MultipartFile pdf) {
        RegulationEntity entity = regulationRepository.findById(regulationId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Normativa no encontrada: " + regulationId));

        if (pdf.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El fichero PDF está vacío");
        }

        try {
            Path dir = Paths.get(pdfStoragePath);
            Files.createDirectories(dir);

            // Usar solo el UUID como nombre para evitar problemas con nombres de fichero con espacios
            String filename = regulationId + ".pdf";
            Path destination = dir.resolve(filename);

            pdf.transferTo(destination);
            entity.setPdfPath(destination.toString());
            regulationRepository.save(entity);

            return destination.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "No se ha podido almacenar el PDF: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Resource getPdf(UUID regulationId) {

        Optional<RegulationEntity> optional = regulationRepository.findById(regulationId);

        if (!optional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Fichero PDF no encontrado en disco"
            );
        }

        RegulationEntity entity = optional.get();

        if (entity.getPdfPath() == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Esta normativa no tiene PDF asociado"
            );
        }

        Path path = Paths.get(entity.getPdfPath());
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Fichero PDF no encontrado en disco"
            );
        }

        return resource;
    }

    private ResponseStatusException regulationNotFound(UUID id) {
        return new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Normativa no encontrada: " + id
        );
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