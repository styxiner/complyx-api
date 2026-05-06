package io.github.styxiner.complyx_api.policies;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/*
 * DTO de actualización de una remediación.
 * Representa la acción correctiva asociada a un check.
 */
@Schema(description = "Datos para actualizar una remediación existente")
public class PolicyRemediationUpdateDTO {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    @Schema(description = "Comando técnico de remediación")
    private String remediationCommand;
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
	public String getRemediationCommand() {
		return remediationCommand;
	}
	public void setRemediationCommand(String remediationCommand) {
		this.remediationCommand = remediationCommand;
	}

}
