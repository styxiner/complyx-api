package io.github.styxiner.complyx_api.agents;

import jakarta.validation.constraints.NotBlank;

/*
 * DTO de entrada para crear un grupo de agentes.
 * Se usa en POST /api/agent-groups
 */
public class AgentGroupCreateDTO {

	@NotBlank
	private String name;
	@NotBlank
	private String description;

	public AgentGroupCreateDTO() {
	}

	public AgentGroupCreateDTO(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}