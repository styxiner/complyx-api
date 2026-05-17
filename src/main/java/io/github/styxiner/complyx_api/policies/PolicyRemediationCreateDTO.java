package io.github.styxiner.complyx_api.policies;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Remediación asociada a un check")
public class PolicyRemediationCreateDTO {

	@NotBlank
	private String name;

	@Schema(description = "Descripción de la acción correctiva")
	private String description;

	@NotNull
	@Schema(description = "Parámetros del remediador. Debe incluir 'type'.", example = "{\"type\":\"file_line_set\",\"path\":\"/etc/login.defs\",\"key\":\"PASS_MIN_LEN\",\"value\":\"15\"}")
	private JsonNode remediationParams;

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