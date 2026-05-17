package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Check dentro de un elemento de policy")
public class PolicyCheckCreateDTO {

	@NotBlank
	private String name;

	@NotNull
	@Schema(description = "Parámetros del executor del agente. Debe incluir 'type'. "
			+ "Tipos disponibles: file_exists, file_absent, file_block, file_line, ini_value, "
			+ "dir_contains, symlink, pkg_installed, pkg_absent, service, sysctl, user_attr", example = "{\"type\":\"sysctl\",\"key\":\"net.ipv4.ip_forward\",\"operator\":\"=\",\"value\":\"0\"}")
	private JsonNode checkParams;

	@Schema(description = "Justificación del check")
	private String rationale;

	@Valid
	@Schema(description = "Acciones de remediación asociadas")
	private List<PolicyRemediationCreateDTO> remediations;

	@Schema(description = "IDs de las secciones de regulación asociadas")
	private List<UUID> regulationSectionIds;

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

	public List<PolicyRemediationCreateDTO> getRemediations() {
		return remediations;
	}

	public void setRemediations(List<PolicyRemediationCreateDTO> remediations) {
		this.remediations = remediations;
	}

	public List<UUID> getRegulationSectionIds() {
		return regulationSectionIds;
	}

	public void setRegulationSectionIds(List<UUID> regulationSectionIds) {
		this.regulationSectionIds = regulationSectionIds;
	}
}