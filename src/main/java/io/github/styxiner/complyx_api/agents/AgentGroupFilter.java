package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

/*
 * DTO de filtro para grupos de agentes.
 * Se usa en endpoints GET /api/groups y se mapea automáticamente desde query params.
 */
public class AgentGroupFilter {
	private String name;
	private String description;
	private UUID agentId; // filtrar grupos que contengan un agente concreto

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

	public UUID getAgentId() {
		return agentId;
	}

	public void setAgentId(UUID agentId) {
		this.agentId = agentId;
	}

}
