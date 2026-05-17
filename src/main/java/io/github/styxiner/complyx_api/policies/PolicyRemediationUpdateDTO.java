package io.github.styxiner.complyx_api.policies;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para actualizar una remediación existente")
public class PolicyRemediationUpdateDTO {

	private UUID id;

	@NotBlank
	private String name;

	private String description;

	@NotNull
	@Schema(description = "Parámetros del remediador. Debe incluir 'type'.", example = "{\"type\":\"service_set\",\"name\":\"auditd\",\"active\":true,\"enabled\":true}")
	private JsonNode remediationParams;

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