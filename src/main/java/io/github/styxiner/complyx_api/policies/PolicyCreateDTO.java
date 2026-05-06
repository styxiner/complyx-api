package io.github.styxiner.complyx_api.policies;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/*
 * DTO de entrada para la creación de una Policy.
 * Permite construir la jerarquía completa (elements, checks, remediations).
 */
@Schema(description = "Solicitud para crear una nueva politica")
public class PolicyCreateDTO {
	@NotBlank
	private String name;
	@NotBlank
	private String version;
	@NotNull
	@Schema(description = "Severidad global de la policy")
	private Severity severity;
	@NotNull
	@Schema(description = "Estado inicial de la politica", example = "DRAFT")
	private PolicyStatus status;
	@Valid
	@NotNull
	@Schema(description = "Lista de elementos que componen la politica")
	private List<PolicyElementCreateDTO> elements;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public List<PolicyElementCreateDTO> getElements() {
		return elements;
	}
	public void setElements(List<PolicyElementCreateDTO> elements) {
		this.elements = elements;
	}
	public PolicyStatus getStatus() {
		return status;
	}
	public void setStatus(PolicyStatus status) {
		this.status = status;
	}

}