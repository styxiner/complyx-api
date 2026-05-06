package io.github.styxiner.complyx_api.policies;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 * DTO de actualización de un elemento de Policy.
 * Permite modificar checks asociados.
 */
@Schema(description = "Elemento de una politica en actualización")
public class PolicyElementUpdateDTO {
	private UUID id;
	@NotBlank
    private String name;
    private String description;
    @Valid
    @NotNull
    @Schema(description = "Checks asociados al elemento")
    private List<PolicyCheckUpdateDTO> checks;
	
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
	public List<PolicyCheckUpdateDTO> getChecks() {
		return checks;
	}
	public void setChecks(List<PolicyCheckUpdateDTO> checks) {
		this.checks = checks;
	}

    
}