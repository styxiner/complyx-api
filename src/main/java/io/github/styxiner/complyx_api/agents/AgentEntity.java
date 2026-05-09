package io.github.styxiner.complyx_api.agents;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.github.styxiner.complyx_api.policies.PolicyEntity;
import jakarta.persistence.*;


@Entity
@Table(name = "agents")
public class AgentEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private String ip;

	@Column(nullable = false)
	private String hostname;

	@Column(nullable = false)
	private String osName;

	@Column(nullable = false)
	private String osVersion;

	@Column(nullable = false, updatable = false)
	private LocalDateTime installDate;

	@Column(nullable = false)
	private LocalDateTime latestConnection;

	@Column(nullable = false)
	private boolean enabled;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "agent_group_membership", joinColumns = @JoinColumn(name = "agent_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<AgentGroupEntity> groups = new HashSet<>();

	@ManyToMany(mappedBy = "agents", fetch = FetchType.LAZY)
	private Set<PolicyEntity> policies = new HashSet<>();

	// Inicializa los valores que deben establecerse automaticamente al crear el agente.
	@PrePersist
	protected void onCreate() {
		this.installDate = LocalDateTime.now();
		this.latestConnection = LocalDateTime.now(); 
		this.enabled = true;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public LocalDateTime getInstallDate() {
		return installDate;
	}

	public void setInstallDate(LocalDateTime installDate) {
		this.installDate = installDate;
	}

	public LocalDateTime getLatestConnection() {
		return latestConnection;
	}

	public void setLatestConnection(LocalDateTime latestConnection) {
		this.latestConnection = latestConnection;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<AgentGroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(Set<AgentGroupEntity> groups) {
		this.groups = groups;
	}

	public Set<PolicyEntity> getPolicies() {
		return policies;
	}

	public void setPolicies(Set<PolicyEntity> policies) {
		this.policies = policies;
	}

	public void addGroup(AgentGroupEntity group) {
		this.groups.add(group);
		group.getAgents().add(this); // Mantener sincronizada la relacion bidireccional
	}

	public void removeGroup(AgentGroupEntity group) {
		this.groups.remove(group);
		group.getAgents().remove(this); // Mantener sincronizada la relacion bidireccional
	}

	// Mantiene sincronizada la relacion bidireccional con Policy en memoria.
	public void addPolicy(PolicyEntity policy) {
		this.policies.add(policy);
		policy.getAgents().add(this);
	}

	public void removePolicy(PolicyEntity policy) {
		this.policies.remove(policy);
		policy.getAgents().remove(this);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AgentEntity)) {
			return false;
		}
		AgentEntity other = (AgentEntity) obj;
		return id != null && id.equals(other.id);
	}
}