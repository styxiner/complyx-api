package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Schema(description = "Check dentro de un elemento de policy")
public class PolicyCheckDTO {
	private UUID id;
	private String name;

	@Schema(description = "Justificación técnica o normativa del chequeo")
	private String rationale;

	@Schema(description = "Parámetros del check en formato JSON. Debe incluir 'type' con uno de: "
			+ "file_exists, file_absent, file_block, file_line, ini_value, dir_contains, "
			+ "symlink, pkg_installed, pkg_absent, service, sysctl, user_attr", example = "{\"type\":\"file_line\",\"path\":\"/etc/login.defs\",\"key\":\"PASS_MIN_LEN\",\"operator\":\">=\",\"value\":\"15\"}")
	private JsonNode checkParams;

	@Schema(description = "Lista de acciones de remediación")
	@Valid
	private List<PolicyRemediationDTO> remediations;

	@Schema(description = "IDs de las secciones de regulación asociadas")
	private List<UUID> regulationSectionIds;

	public PolicyCheckDTO() {
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

	public String getRationale() {
		return rationale;
	}

	public void setRationale(String rationale) {
		this.rationale = rationale;
	}

	public JsonNode getCheckParams() {
		return checkParams;
	}

	public void setCheckParams(JsonNode checkParams) {
		this.checkParams = checkParams;
	}

	public List<PolicyRemediationDTO> getRemediations() {
		return remediations;
	}

	public void setRemediations(List<PolicyRemediationDTO> remediations) {
		this.remediations = remediations;
	}

	public List<UUID> getRegulationSectionIds() {
		return regulationSectionIds;
	}

	public void setRegulationSectionIds(List<UUID> regulationSectionIds) {
		this.regulationSectionIds = regulationSectionIds;
	}
}