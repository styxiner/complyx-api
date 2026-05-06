package io.github.styxiner.complyx_api.policies;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/*
 * DTO que representa una acción de remediación para un PolicyCheck.
 * Define qué hacer cuando un check no se cumple.
 */
@Schema(description = "Acción de remediación asociada a un check")
public class PolicyRemediationDTO {
	private UUID id;
	private String name;
	@Schema(description = "Descripción de la acción correctiva")
	private String description;

	public PolicyRemediationDTO() {
	}

	public PolicyRemediationDTO(UUID id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
