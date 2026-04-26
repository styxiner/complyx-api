package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.UUID;

/*
 * Representa los datos que se ennvian al frontend/cliente
 */
public class AgentDTO {
	private UUID id;
	private String ip;
	private String hostname;
	private String osName;
	private String osVersion;
	private boolean enabled;
	private List<String> groups;

	public AgentDTO(UUID id, String ip, String hostname, String osName, String osVersion, boolean enabled,
			List<String> groups) {
		super();
		this.id = id;
		this.ip = ip;
		this.hostname = hostname;
		this.osName = osName;
		this.osVersion = osVersion;
		this.enabled = enabled;
		this.groups = groups;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

}
