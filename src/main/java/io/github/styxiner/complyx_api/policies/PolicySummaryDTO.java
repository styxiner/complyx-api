package io.github.styxiner.complyx_api.policies;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
/*Clase para mostrar información ligera, rápida y suficiente
 * para listar policies, sin cargar toda la jerarquía.
 */
@Schema(description = "Resumen de una policy para listados")
public class PolicySummaryDTO {
	private UUID id;
	private String name;
	private String version;
@Schema(description = "Nivel de severidad de la policy")
	private Severity severity;
	private String status;
	private LocalDateTime createdAt;

	public PolicySummaryDTO() {
	}

	public PolicySummaryDTO(UUID id, String name, String version, Severity severity, String status,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.severity = severity;
		this.status = status;
		this.createdAt = createdAt;
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

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
