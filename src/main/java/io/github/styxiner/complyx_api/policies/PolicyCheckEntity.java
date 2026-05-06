package io.github.styxiner.complyx_api.policies;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.github.styxiner.complyx_api.regulations.RegulationSectionEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "policy_checks")
public class PolicyCheckEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private String name;
	private String rationale;

	@Column(name = "check_command")
	private String checkCommand;

	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;
//este es el dueño de la relacion, es el lado que tiene la FK
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_element_id", nullable = false)
	private PolicyElementEntity policyElement;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "check_regulation_sections", joinColumns = @JoinColumn(name = "check_id"), inverseJoinColumns = @JoinColumn(name = "regulation_section_id"))
	private Set<RegulationSectionEntity> regulationSections = new HashSet<>();
	/*
	 * No reemplazar la colección directamente. Usar
	 * addRemediation/removeRemediation.
	 */
	@OneToMany(mappedBy = "policyCheck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PolicyRemediationEntity> remediations = new ArrayList<>();

	public PolicyCheckEntity() {
	}

	public PolicyCheckEntity(UUID id, String name, String rationale, String checkCommand, LocalDateTime createdDate,
			PolicyElementEntity policyElement, Set<RegulationSectionEntity> regulationSections,
			List<PolicyRemediationEntity> remediations) {
		super();
		this.id = id;
		this.name = name;
		this.rationale = rationale;
		this.checkCommand = checkCommand;
		this.createdDate = createdDate;
		this.policyElement = policyElement;
		this.regulationSections = regulationSections;
		this.remediations = remediations;
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

	public String getRationale() {
		return rationale;
	}

	public void setRationale(String rationale) {
		this.rationale = rationale;
	}

	public String getCheckCommand() {
		return checkCommand;
	}

	public void setCheckCommand(String checkCommand) {
		this.checkCommand = checkCommand;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public PolicyElementEntity getPolicyElement() {
		return policyElement;
	}

	public void setPolicyElement(PolicyElementEntity policyElement) {
		this.policyElement = policyElement;
	}

	public Set<RegulationSectionEntity> getRegulationSections() {
		return regulationSections;
	}

	public void setRegulationSections(Set<RegulationSectionEntity> regulationSections) {
		this.regulationSections = regulationSections;
	}
	public List<PolicyRemediationEntity> getRemediations() {
		return remediations;
	}

	public void setRemediations(List<PolicyRemediationEntity> remediations) {
		this.remediations = remediations;
	}

	// Añade una remediation manteniendo la relación bidireccional .

	public void addRemediation(PolicyRemediationEntity remediation) {
		remediations.add(remediation);
		remediation.setPolicyCheck(this);
	}

	// Elimina una remediation y gracias a orphanRemoval (DELETE automático en BD).

	public void removeRemediation(PolicyRemediationEntity remediation) {
		remediations.remove(remediation);
		remediation.setPolicyCheck(null);
	}

	// Añade una regulation section.

	public void addRegulationSection(RegulationSectionEntity section) {
		regulationSections.add(section);
	}

	public void removeRegulationSection(RegulationSectionEntity section) {
		regulationSections.remove(section);
	}

	// Se ejecuta automáticamente antes del INSERT.Garantiza consistencia sin
	// depender del Service.

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PolicyCheckEntity that = (PolicyCheckEntity) obj;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
