package io.github.styxiner.complyx_api.regulations;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import io.github.styxiner.complyx_api.policies.PolicyCheckEntity;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegulationSectionEntity {
	private String title;
	private Set<PolicyCheckEntity> checks;
}
