package io.github.styxiner.complyx_api.agents;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.github.styxiner.complyx_api.policies.PolicyEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "agent_groups")
public class AgentGroupEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false, unique = true)
	private String name;

	private String description;

	@ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
	private Set<AgentEntity> agents = new HashSet<>();

	@ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
	private Set<PolicyEntity> policies = new HashSet<>();

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

	public Set<AgentEntity> getAgents() {
		return agents;
	}

	public void setAgents(Set<AgentEntity> agents) {
		this.agents = agents;
	}

	public Set<PolicyEntity> getPolicies() {
		return policies;
	}

	public void setPolicies(Set<PolicyEntity> policies) {
		this.policies = policies;
	}

	// Mantiene sincronizada la relacion bidireccional con Policy.
	public void addPolicy(PolicyEntity policy) {
		this.policies.add(policy);
		policy.getGroups().add(this);
	}

	public void removePolicy(PolicyEntity policy) {
		this.policies.remove(policy);
		policy.getGroups().remove(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AgentGroupEntity)) {
			return false;
		}
		AgentGroupEntity other = (AgentGroupEntity) obj;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}