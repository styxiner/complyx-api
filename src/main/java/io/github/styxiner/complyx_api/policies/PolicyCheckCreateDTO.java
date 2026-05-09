package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * DTO de entrada para crear un check dentro de un elemento.
 * Define reglas, severidad y acciones correctivas.
 */
@Schema(description = "Check dentro de un elemento de policy")
public class PolicyCheckCreateDTO {
	@NotBlank
	private String name;
	@NotBlank
	@Schema(description = "Comando de validaciÃ³n")
	private String checkCommand;
	@Schema(description = "JustificaciÃ³n del check")
	private String rationale;
	@Valid
	@Schema(description = "Acciones de remediaciÃ³n asiciadas")
	private List<PolicyRemediationCreateDTO> remediations;
	@Schema(description = "IDs de las secciones de regulaciÃ³n asociadas")
	private List<UUID> regulationSectionIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheckCommand() {
		return checkCommand;
	}

	public void setCheckCommand(String checkCommand) {
		this.checkCommand = checkCommand;
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
