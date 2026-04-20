package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.UUID;

public class RegulationDetailDTO {
	private UUID id;
	private String name;
	private String pdfPath;
	private List<RegSectionDTO> sections;
}
