package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
/*
 * DTO que representa un elemento dentro de una Policy.
 * Contiene información básica y la lista de checks asociados.
 */
@Schema(description = "Elemento dentro de una policy")
public class PolicyElementDTO {
	private UUID id;
	private String name;
	@Schema(description = "Lista de checks asociados a este elemento")
	private List<PolicyCheckDTO> checks;

	public PolicyElementDTO() {

	}

	public PolicyElementDTO(UUID id, String name, List<PolicyCheckDTO> checks) {
		super();
		this.id = id;
		this.name = name;
		this.checks = checks;
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

	public List<PolicyCheckDTO> getChecks() {
		return checks;
	}

	public void setChecks(List<PolicyCheckDTO> checks) {
		this.checks = checks;
	}

}
