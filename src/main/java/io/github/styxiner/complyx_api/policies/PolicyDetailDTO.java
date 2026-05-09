package io.github.styxiner.complyx_api.policies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
/* DTO de salida que representa el detalle completo de una Policy.
 * Se utiliza en el endpoint GET /policies/{id} y contiene toda la estructura
 * jerárquica asociada a la policy necesarias para visualizar o editar una policy en profundidad.
 */
@Schema(description = "Detalle completo de una policy")
public class PolicyDetailDTO {
	private UUID id;
	private String name;
	private String version;
	private String description;
	@Schema(description = "Estado de la policy", example = "ACTIVE")	
	private String status;
    @Schema(description = "Severidad global de la policy")
	private Severity severity;
    @Schema(description = "Fecha de creación de la policy")
	private LocalDateTime createdAt;
    @Schema(description = "Lista jerárquica de elementos de la policy")
	private List<PolicyElementDTO> elements;

	public PolicyDetailDTO() {
	}

	public PolicyDetailDTO(UUID id, String name, String version, String description, String status, Severity severity,
			LocalDateTime createdAt, List<PolicyElementDTO> elements) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.description = description;
		this.status = status;
		this.severity = severity;
		this.createdAt = createdAt;
		this.elements = elements;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<PolicyElementDTO> getElements() {
		return elements;
	}

	public void setElements(List<PolicyElementDTO> elements) {
		this.elements = elements;
	}

}
