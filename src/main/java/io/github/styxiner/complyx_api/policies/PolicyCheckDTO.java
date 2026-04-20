package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

public class PolicyCheckDTO {
	private UUID id;
	private String name;
	private String rationale;
	private List<PolicyRemediationDTO> remediations;
}
