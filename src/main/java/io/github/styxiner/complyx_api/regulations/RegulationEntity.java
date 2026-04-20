package io.github.styxiner.complyx_api.regulations;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegulationEntity {
	private String name;
	private String pdfPath;
	private List<RegulationSectionEntity> sections;
}
