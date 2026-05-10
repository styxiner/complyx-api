package io.github.styxiner.complyx_api.risk_modeling;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThreatService {

    private final ThreatRepository threatRepository;
    private final RiskMapper mapper;

    public Page<ThreatDTO> findAll(Pageable pageable) {
        return threatRepository.findAll(pageable)
                .map(mapper::toThreatDTO);
    }

    public ThreatDTO findById(UUID threatId) {
        return threatRepository.findById(threatId)
                .map(mapper::toThreatDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + threatId));
    }

    @Transactional
    public ThreatDTO create(ThreatCreateDTO dto) {
        if (threatRepository.existsByName(dto.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe una amenaza con el nombre: " + dto.name());
        }

        ThreatEntity entity = ThreatEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .category(dto.category())
                .severityScore(dto.severityScore())
                .build();

        return mapper.toThreatDTO(threatRepository.save(entity));
    }

    @Transactional
    public ThreatDTO update(UUID threatId, ThreatUpdateDTO dto) {
        ThreatEntity entity = threatRepository.findById(threatId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + threatId));

        mapper.updateThreatFromDTO(dto, entity);
        return mapper.toThreatDTO(threatRepository.save(entity));
    }

    @Transactional
    public void delete(UUID threatId) {
        if (!threatRepository.existsById(threatId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + threatId);
        }
        // ON DELETE RESTRICT: falla si hay riesgos asociados
        threatRepository.deleteById(threatId);
    }
}