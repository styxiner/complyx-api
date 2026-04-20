package io.github.styxiner.complyx_api.policies;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRemediationEntity {
	private String name;
	private String description;
	private String remediationCommand;
}
