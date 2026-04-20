package io.github.styxiner.complyx_api.policies;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import io.github.styxiner.complyx_api.regulations.RegulationSectionEntity;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCheckEntity {
	private String name;
	private String rationale;
	private String checkCommand;
	private Set<RegulationSectionEntity> regulationSections;
	private List<PolicyRemediationEntity> remediations;
}
