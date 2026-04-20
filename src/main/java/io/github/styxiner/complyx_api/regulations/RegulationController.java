package io.github.styxiner.complyx_api.regulations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class RegulationController {
	Page<RegulationSummaryDTO> getRegulations(RegulationFilter filter, Pageable pageable) {return null;}
	RegulationDetailDTO getRegulationById(UUID regulationId) {return null;}
	ResponseEntity<RegulationDetailDTO> createRegulation(RegulationCreateDTO regulationCreateDto) {return null;}
	RegulationDetailDTO updateRegulation(UUID regulationId, RegulationUpdateDTO regulationUpdateDto) {return null;}
	ResponseEntity<Void> deleteRegulation(UUID regulationId) {return null;}
	ResponseEntity<Void> uploadPdf(UUID regulationId, MultipartFile pdf) {return null;}
	RegulationDetailDTO addSection(UUID regulationId, RegSectionCreateDTO regSectionDto) {return null;}
}
