package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para actualizar un chequeo")
public class PolicyCheckUpdateDTO {

	private UUID id;

	@NotBlank
	private String name;

	@NotNull
	@Schema(description = "Parámetros del executor del agente. Debe incluir 'type'.", example = "{\"type\":\"service\",\"name\":\"sshd\",\"active\":true,\"enabled\":true}")
	private JsonNode checkParams;

	@Schema(description = "Justificación del check")
	private String rationale;

	@Valid
	@Schema(description = "Remediaciones asociadas")
	private List<PolicyRemediationUpdateDTO> remediations;

	@Schema(description = "IDs de las secciones regulatorias")
	private List<UUID> regulationSectionIds;

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

	public JsonNode getCheckParams() {
		return checkParams;
	}

	public void setCheckParams(JsonNode checkParams) {
		this.checkParams = checkParams;
	}

	public String getRationale() {
		return rationale;
	}

	public void setRationale(String rationale) {
		this.rationale = rationale;
	}

	public List<PolicyRemediationUpdateDTO> getRemediations() {
		return remediations;
	}

	public void setRemediations(List<PolicyRemediationUpdateDTO> remediations) {
		this.remediations = remediations;
	}

	public List<UUID> getRegulationSectionIds() {
		return regulationSectionIds;
	}

	public void setRegulationSectionIds(List<UUID> regulationSectionIds) {
		this.regulationSectionIds = regulationSectionIds;
	}
}