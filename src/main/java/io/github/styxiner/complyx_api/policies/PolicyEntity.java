package io.github.styxiner.complyx_api.policies;

import java.time.LocalDateTime;
import java.util.*;

import io.github.styxiner.complyx_api.agents.AgentEntity;
import io.github.styxiner.complyx_api.agents.AgentGroupEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.*;

/*Entidad principal de Policy con las relaciones  aAgentEntity y  AgentGroupEntity*/
@Entity
@Table(name = "policies")
@Schema(description = "Entidad principal que define una normativa o política de cumplimiento")
public class PolicyEntity {
	@Id
	@GeneratedValue
	private UUID id;
	@Column(nullable = false, unique = true)
	private String name;
	private String version;
	@Column(columnDefinition = "TEXT")
	private String description;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PolicyStatus status = PolicyStatus.DRAFT; // Valor por defecto
	@Enumerated(EnumType.STRING)
	private Severity severity;
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column(name = "last_modified")
	private LocalDateTime lastModified;
	/*
	 * CascadeType.ALL:El elemento se persistirá automáticamente al guardar la
	 * policy (los elements,checks, remediations...) OrphanRemoval=true:Hibernate
	 * podrá ejecutar automáticamente un DELETE en BD. Esta colección NO debe
	 * reemplazarse directamente (setElements) en operaciones de update. Se deben
	 * usar métodos helper (addElement/removeElement) para mantener la consistencia
	 * JPA y evitar problemas con orphanRemoval.
	 */
	@OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PolicyElementEntity> elements = new ArrayList<>();

	// Politicas asignadas a agentes.

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "policy_agents", joinColumns = @JoinColumn(name = "policy_id"), inverseJoinColumns = @JoinColumn(name = "agent_id"))
	private Set<AgentEntity> agents = new HashSet<>();
	// Politicas asignadas a grupos de agentes.
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "policy_groups", joinColumns = @JoinColumn(name = "policy_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<AgentGroupEntity> groups = new HashSet<>();

	public PolicyEntity() {
	}

	public PolicyEntity(String name, String version, String description, Severity severity) {
		super();
		this.name = name;
		this.version = version;
		this.description = description;
		this.severity = severity;
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

	public PolicyStatus getStatus() {
		return status;
	}

	public void setStatus(PolicyStatus status) {
		this.status = status;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}


	public List<PolicyElementEntity> getElements() {
		return elements;
	}

	public void setElements(List<PolicyElementEntity> elements) {
		this.elements = elements;
	}

	public Set<AgentEntity> getAgents() {
		return agents;
	}

	public void setAgents(Set<AgentEntity> agents) {
		this.agents = agents;
	}

	public Set<AgentGroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(Set<AgentGroupEntity> groups) {
		this.groups = groups;
	}

	/*
	 * Este método garantiza consistencia en memoria antes de persistir. En JPA las
	 * relaciones bidireccionales NO se sincronizan automáticamente. Hibernate NO
	 * actualizará correctamente la FK por lo que asignamos this como policy en el
	 * lado hijo
	 */
	public void addElement(PolicyElementEntity element) {
		elements.add(element);
		element.setPolicy(this);
	}

	/*
	 * Elimina un PolicyElement de la Policy manteniendo la relación bidireccional
	 * sincronizada. Se elimina del listado (lado padre)y se rompe la relación en el
	 * hijo (policy = null)
	 */
	public void removeElement(PolicyElementEntity element) {
		elements.remove(element);
		element.setPolicy(null);
	}

	/*
	 * Añade un agente a la politica. Mantiene sincronizada la relación
	 * bidireccionaly Evita inconsistencias en memoria
	 */
	public void addAgent(AgentEntity agent) {
		this.agents.add(agent);
		agent.getPolicies().add(this); // sincronización bidireccional
	}

	/* Elimina un agente de la politica */
	public void removeAgent(AgentEntity agent) {
		this.agents.remove(agent);
		agent.getPolicies().remove(this);
	}

	/* Añade un grupo de la politica */
	public void addGroup(AgentGroupEntity group) {
		this.groups.add(group);
		group.getPolicies().add(this);
	}

	/* Elimina un grupo de la politica */
	public void removeGroup(AgentGroupEntity group) {
		this.groups.remove(group);
		group.getPolicies().remove(this);
	}

	/*
	 * Callback de JPA que se ejecuta automáticamente ANTES de hacer un INSERT.
	 * Garantiza que la fecha de creación SIEMPRE se establezca, independientemente
	 * de desde dónde se persista la entidad y Evita duplicar lógica en múltiples
	 * métodos del Service
	 */
	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now();
	}

	/*
	 * Callback de JPA que se ejecuta automáticamente ANTES de hacer un UPDATE.
	 * Asegura consistencia de datos en BD y mantiene la lógica de auditoría básica
	 * encapsulada dentro de la entidad
	 */
	@PreUpdate
	protected void onUpdate() {
		this.lastModified = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PolicyEntity that = (PolicyEntity) obj;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
