package io.github.styxiner.complyx_api.policies;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
/*
 * DTO de entrada para crear un elemento dentro de una Policy.
 * Contiene los checks asociados que forman parte del elemento.
 */
@Schema(description = "Elemento de una politica en creación")
public class PolicyElementCreateDTO {
    @NotBlank
    private String name;
    private String description;
    @Valid
    @NotNull
    @Schema(description = "Checks asociados al elemento")
    private List<PolicyCheckCreateDTO> checks;
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
	public List<PolicyCheckCreateDTO> getChecks() {
		return checks;
	}
	public void setChecks(List<PolicyCheckCreateDTO> checks) {
		this.checks = checks;
	}
    
}