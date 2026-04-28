package io.github.styxiner.complyx_api.agents;

import java.util.UUID;
/*
 * DTO de filtro para la búsqueda de agentes.
 * Se usa en endpoints GET /api/agents y se mapea automáticamente desde query params.
 */
public class AgentFilter {
	private String ip;
	private String hostname;
	private String osName;
	private Boolean enabled;
	private UUID groupId;
	
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
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public UUID getGroupId() {
		return groupId;
	}
	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}
	
}
