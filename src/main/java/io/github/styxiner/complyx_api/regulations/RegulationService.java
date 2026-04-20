package io.github.styxiner.complyx_api.regulations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public class RegulationService {
	Page<RegulationSummaryDTO> findAll(RegulationFilter filter, Pageable pageable) {return null;}
	RegulationDetailDTO findById(UUID regulationId) {return null;}
	RegulationDetailDTO create(RegulationCreateDTO regulationCreateDto) {return null;}
	RegulationDetailDTO update(UUID regulationId, RegulationUpdateDTO regulationUpdateDto) {return null;}
	void delete(UUID regulationId) {}
	String storePdf(UUID regulationId, MultipartFile pdf) {return null;}
	RegulationDetailDTO addSection(UUID regulationId, RegSectionCreateDTO section) {return null;}
}
