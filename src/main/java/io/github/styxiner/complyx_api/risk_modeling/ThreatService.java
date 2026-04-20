package io.github.styxiner.complyx_api.risk_modeling;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ThreatService {
	Page<ThreatDTO> findAll(Pageable pageable) {return null;}
	ThreatDTO findById(UUID threatId) {return null;}
	ThreatDTO create(ThreatCreateDTO threatCreateDto) {return null;}
	ThreatDTO update(ThreatUpdateDTO threatUpdateDto) {return null;}
	void delete(UUID threatId) {}
}
