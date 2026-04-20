package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

public class PolicyDetailDTO {
	private UUID id;
	private String name;
	private String description;
	private Severity severity;
	private List<PolicyElementDTO> elements;
}
