package io.github.styxiner.complyx_api.policies;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * DTO de entrada para actualizar una Policy.
 * Permite modificar datos b�sicos.
 */
@Schema(description = "Solicitud para actualizar una politica existente")
public class PolicyUpdateDTO {
	@NotBlank
	private String name;
	@NotBlank
	private String version;
	@NotBlank
	private String description;
	@NotNull
	@Schema(description = "Severidad global de la politica")
	private Severity severity;
	@NotNull
	@Schema(description = "Estado de la politica")
	private PolicyStatus status;
	@Valid
	@NotNull
	@Schema(description = "Lista de elementos de la politica")
	private List<PolicyElementUpdateDTO> elements;
	
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
	public PolicyStatus getStatus() {
		return status;
	}
	public void setStatus(PolicyStatus status) {
		this.status = status;
	}
	public List<PolicyElementUpdateDTO> getElements() {
		return elements;
	}
	public void setElements(List<PolicyElementUpdateDTO> elements) {
		this.elements = elements;
	}
	public String getDescription() {
		return description;
	}
	
}
