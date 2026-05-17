package io.github.styxiner.complyx_api.policies;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Acción de remediación asociada a un check")
public class PolicyRemediationDTO {

	private UUID id;
	private String name;

	@Schema(description = "Descripción de la acción correctiva")
	private String description;

	@Schema(description = "Parámetros del remediador en formato JSON. Debe incluir 'type' con uno de: "
			+ "file_line_set, file_block_set, pkg_install, pkg_remove, service_set, sysctl_set", example = "{\"type\":\"sysctl_set\",\"key\":\"net.ipv4.ip_forward\",\"value\":\"0\"}")
	private JsonNode remediationParams;

	public PolicyRemediationDTO() {
	}

	public PolicyRemediationDTO(UUID id, String name, String description, JsonNode remediationParams) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.remediationParams = remediationParams;
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

	public JsonNode getRemediationParams() {
		return remediationParams;
	}

	public void setRemediationParams(JsonNode remediationParams) {
		this.remediationParams = remediationParams;
	}
}