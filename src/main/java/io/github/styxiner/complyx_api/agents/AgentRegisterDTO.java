package io.github.styxiner.complyx_api.agents;

import jakarta.validation.constraints.NotBlank;

/*
 * DTO de entrada para registrar un nuevo agente.
 * Representa los datos que envía el cliente/agent/frontend
 */
public class AgentRegisterDTO {
	@NotBlank
	private String ip;
	@NotBlank
	private String hostname;
	@NotBlank
	private String osName;
	@NotBlank
	private String osVersion;
	public AgentRegisterDTO(String ip, String hostname, String osName, String osVersion) {
		super();
		this.ip = ip;
		this.hostname = hostname;
		this.osName = osName;
		this.osVersion = osVersion;
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
	
}
