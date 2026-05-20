package io.github.styxiner.complyx_api.policies;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

/*Objeto de consulta utilizado para construir dinámicamente Specifications 
 *de búsqueda sobre PolicyEntity contiene criterios de filtrado.
 */
@Schema(name = "PolicyFilter", description = "Filtros disponibles para búsqueda dinámica de policies")
public class PolicyFilter {
	private String name;
	@Schema(description = "Filtrar por severidad", example = "HIGH")
	private Severity severity;
	@Schema(description = "Filtrar por agente asignado")
	private UUID assignedToAgentId;
	@Schema(description = "Filtrar por grupo asignado")
	private UUID assignedToGroupId;
	@Schema(description = "Filtrar por normativa asociada")
	private UUID regulationId;
	@Schema(description = "Filtrar por estado de la política")
	private PolicyStatus status;
	/*
	 * Indica si deben incluirse también las politicas que no están asignadas a
	 * ningún agente ni grupo; true:todas; false: solo politicas asignadas según los
	 * filtros aplicados; null:comportamiento por defecto definido en la
	 * Specification
	 */
	private Boolean includeUnassigned;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public UUID getAssignedToAgentId() {
		return assignedToAgentId;
	}

	public void setAssignedToAgentId(UUID assignedToAgentId) {
		this.assignedToAgentId = assignedToAgentId;
	}

	public UUID getAssignedToGroupId() {
		return assignedToGroupId;
	}

	public void setAssignedToGroupId(UUID assignedToGroupId) {
		this.assignedToGroupId = assignedToGroupId;
	}

	public Boolean getIncludeUnassigned() {
		return includeUnassigned;
	}

	public void setIncludeUnassigned(Boolean includeUnassigned) {
		this.includeUnassigned = includeUnassigned;
	}

	public UUID getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(UUID regulationId) {
		this.regulationId = regulationId;
	}

	public PolicyStatus getStatus() {
		return status;
	}

	public void setStatus(PolicyStatus status) {
		this.status = status;
	}

}
