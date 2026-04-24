package io.github.styxiner.complyx_api.agents;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import java.util.UUID;

/*
 * Representa un grupo de agentes.
 * Se usa para organizar endpoints en categorías:producción,desarrollo,servidores etc.
 */
@Entity
@Table(name="agent_groups")
public class AgentGroupEntity {
	@Id
	@GeneratedValue
	private UUID id;
	@Column(nullable=false, unique=true)
	private String name;
	private String description;
	@ManyToMany(mappedBy="groups",fetch=FetchType.LAZY)
	private Set<AgentEntity> agents =new HashSet<>();
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

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AgentGroupEntity)) return false;
        AgentGroupEntity that = (AgentGroupEntity) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
	
}
