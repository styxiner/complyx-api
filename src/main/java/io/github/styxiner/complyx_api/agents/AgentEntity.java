package io.github.styxiner.complyx_api.agents;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
/*
 * Un agente es el software instalado en cada endpoint (Linux/Windows) que se comunica
 * con el servidor de orquestación para reportar su estado de cumplimiento normativo.
 * Esta clase mapea directamente a una tabla en la base de datos.
 */

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

	/*
	 * LAZY:no carga los grupos a menos que se necesiten. Inicializada para evitar
	 * NullPointerException.
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "agent_group_membership", joinColumns = @JoinColumn(name = "agent_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<AgentGroupEntity> groups = new HashSet<>();

	/*
	 * Método que se ejecuta automáticamente antes de persistir (para inicializar
	 * valores por defecto), asigna installDate, activa el agente y es muy útil para
	 * evitar lógica repetida en el service
	 */
	@PrePersist
	protected void onCreate() {
		this.installDate = LocalDateTime.now();
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

//Añade un grupo del agente
	public void addGroup(AgentGroupEntity group) {
		this.groups.add(group);
	}

//Elimina un grupo del agente
	public void removeGroup(AgentGroupEntity group) {
		this.groups.remove(group);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();// Evita inconsistencias cuando el ID cambia
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AgentEntity)) // instanceOf pues Hibernate crea objetos intermediarios en tiempo de
											// ejecución
			return false;
		return id != null && id.equals(((AgentEntity) obj).id); // Si no hay ID, no son iguales
	}

}
