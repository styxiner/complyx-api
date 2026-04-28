package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

/*
 * DTO de salida para grupos de agentes.
 * Representa lo que el frontend/cliente recibe.
 */
public class AgentGroupDTO {
	private UUID id;
	private String name;
	private String description;
	private List<String> agents;

	public AgentGroupDTO(UUID id, String name, String description, List<String> agents) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.agents = agents;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public List<String> getAgents() {
		return agents;
	}

	public void setAgents(List<String> agents) {
		this.agents = agents;
	}

}