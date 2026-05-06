package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/*
 * DTO de actualización de un check dentro de un elemento.
 * Define reglas, severidad y acciones correctivas.
 */
@Schema(description = "Datos para actualizar un chequeo")
public class PolicyCheckUpdateDTO {
	private UUID id;
	@NotBlank
	private String name;
	private String description;
	@NotBlank
	@Schema(description = "Comando técnico de validación")
	private String checkCommand;
	@Schema(description = "Severidad del check")
	private Severity severity;
	@Schema(description = "Justificación del check")
	private String rationale;
	@Valid
	@Schema(description = "Remediaciones asociadas")
	private List<PolicyRemediationUpdateDTO> remediations;
	@Schema(description = "IDs de las secciones de regulatorias")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCheckCommand() {
		return checkCommand;
	}

	public void setCheckCommand(String checkCommand) {
		this.checkCommand = checkCommand;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
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
