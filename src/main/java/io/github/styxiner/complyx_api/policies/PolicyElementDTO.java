package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

public class PolicyElementDTO {
	private UUID id;
	private String name;
	private List<PolicyCheckDTO> checks;
}
