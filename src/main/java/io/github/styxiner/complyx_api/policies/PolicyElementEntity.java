package io.github.styxiner.complyx_api.policies;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "policy_elements")
public class PolicyElementEntity {
	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private String name;
//@JoinColum indica que aquí se guarda la FK e hibernate usa este lado para persistir la relación
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id", nullable = false)
	private PolicyEntity policy;
	/*
	 * NO reemplazar la colección directamente. Usar addCheck/removeCheck para
	 * mantener consistencia.
	 */
	@OneToMany(mappedBy = "policyElement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PolicyCheckEntity> checks = new ArrayList<>();

	public PolicyElementEntity() {
	}

	public PolicyElementEntity(String name) {
		this.name = name;
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

	public PolicyEntity getPolicy() {
		return policy;
	}

	public void setPolicy(PolicyEntity policy) {
		this.policy = policy;
	}

	public List<PolicyCheckEntity> getChecks() {
		return checks;
	}

	public void setChecks(List<PolicyCheckEntity> checks) {
		this.checks = checks;
	}

	/*
	 * Añade un PolicyCheck manteniendo sincronizada la relación bidireccional. Se
	 * añade al listado (lado padre),se asigna este element como owner en el hijo
	 * para que Hibernate gestiona correctamente la FK.
	 */
	public void addCheck(PolicyCheckEntity check) {
		checks.add(check);
		check.setPolicyElement(this);
	}

	/*
	 * Elimina un PolicyCheck manteniendo la relación consistente. gracias
	 * CascadeType.ALL:El check se persistirá automáticamente al guardar el
	 * element/policy
	 */
	public void removeCheck(PolicyCheckEntity check) {
		checks.remove(check);
		check.setPolicyElement(null);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PolicyElementEntity that = (PolicyElementEntity) obj;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
