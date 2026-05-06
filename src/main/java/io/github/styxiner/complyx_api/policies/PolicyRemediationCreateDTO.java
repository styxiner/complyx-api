package io.github.styxiner.complyx_api.policies;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/*
 * DTO de entrada para definir una acción de remediación.
 * Representa qué hacer cuando un check no se cumple.
 */
@Schema(description = "Remediación asociada a un check")
public class PolicyRemediationCreateDTO {
	@NotBlank
    private String name;
	@NotBlank
	@Schema(description = "Descripción de la acción correctiva")
	private String description;
	@NotBlank
    @Schema(description = "Comando técnico a ejecutar para remediar", example = "ansible-playbook update_tls.yml")
    private String remediationCommand;
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
