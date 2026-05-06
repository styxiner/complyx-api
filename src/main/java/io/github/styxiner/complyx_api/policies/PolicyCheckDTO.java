package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.media.Schema;

/*
 * DTO de entrada para crear un check dentro de un elemento.
 * Permite definir severidad, remediaciones y referencias externas.
 */
@Schema(description = "Check dentro de un elemento de policy")
public class PolicyCheckDTO {
	private UUID id;
	private String name;
	@Schema(description = "Justificación técnica o normativa del chequeo")
	private String rationale;	@Schema(description = "Comando o script técnico que ejecuta la validación")
	private String checkCommand;
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

	public String getCheckCommand() {
		return checkCommand;
	}

	public void setCheckCommand(String checkCommand) {
		this.checkCommand = checkCommand;
	}

}
