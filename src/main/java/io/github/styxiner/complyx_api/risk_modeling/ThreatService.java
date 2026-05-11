package io.github.styxiner.complyx_api.risk_modeling;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ThreatService {

    private final ThreatRepository threatRepository;
    private final RiskMapper mapper;

    public ThreatService(ThreatRepository threatRepository, RiskMapper mapper) {
        this.threatRepository = threatRepository;
        this.mapper = mapper;
    }

    public Page<ThreatDTO> findAll(Pageable pageable) {
        return threatRepository.findAll(pageable)
                .map(new java.util.function.Function<ThreatEntity, ThreatDTO>() {
                    @Override
                    public ThreatDTO apply(ThreatEntity entity) {
                        return mapper.toThreatDTO(entity);
                    }
                });
    }

    public ThreatDTO findById(UUID threatId) {
        Optional<ThreatEntity> opt = threatRepository.findById(threatId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + threatId);
        }
        return mapper.toThreatDTO(opt.get());
    }

    @Transactional
    public ThreatDTO create(ThreatCreateDTO dto) {
        if (threatRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una amenaza con el nombre: " + dto.getName());
        }
        ThreatEntity entity = new ThreatEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        entity.setSeverityScore(dto.getSeverityScore());
        return mapper.toThreatDTO(threatRepository.save(entity));
    }

    @Transactional
    public ThreatDTO update(UUID threatId, ThreatUpdateDTO dto) {
        Optional<ThreatEntity> opt = threatRepository.findById(threatId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + threatId);
        }
        ThreatEntity entity = opt.get();
        mapper.updateThreatFromDTO(dto, entity);
        return mapper.toThreatDTO(threatRepository.save(entity));
    }

    @Transactional
    public void delete(UUID threatId) {
        if (!threatRepository.existsById(threatId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + threatId);
        }
        threatRepository.deleteById(threatId);
    }
}