package io.github.styxiner.complyx_api.policies;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "policy_remediations")
public class PolicyRemediationEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	/**
	 * Parámetros del remediador en formato JSON. Debe contener "type" + los
	 * parámetros específicos del remediador del agente.
	 *
	 * Tipos disponibles y ejemplos:
	 *
	 * file_line_set: { "type": "file_line_set", "path": "/etc/login.defs", "key":
	 * "PASS_MIN_LEN", "value": "15" } file_block_set: { "type": "file_block_set",
	 * "path": "/etc/pam.d/common-password", "block": "password requisite
	 * pam_pwquality.so minlen=15", "backup": true } pkg_install: { "type":
	 * "pkg_install", "name": "auditd", "package_manager": "auto" } pkg_remove: {
	 * "type": "pkg_remove", "name": "telnetd", "purge": true } service_set: {
	 * "type": "service_set", "name": "auditd", "active": true, "enabled": true }
	 * sysctl_set: { "type": "sysctl_set", "key": "net.ipv4.ip_forward", "value":
	 * "0" }
	 */
	@Column(name = "remediation_params", columnDefinition = "jsonb", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private String remediationParams;

	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_check_id", nullable = false)
	private PolicyCheckEntity policyCheck;

	public PolicyRemediationEntity() {
	}

	public PolicyRemediationEntity(String name, String description, String remediationParams) {
		this.name = name;
		this.description = description;
		this.remediationParams = remediationParams;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemediationParams() {
		return remediationParams;
	}

	public void setRemediationParams(String remediationParams) {
		this.remediationParams = remediationParams;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public PolicyCheckEntity getPolicyCheck() {
		return policyCheck;
	}

	public void setPolicyCheck(PolicyCheckEntity policyCheck) {
		this.policyCheck = policyCheck;
	}

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
		PolicyRemediationEntity that = (PolicyRemediationEntity) obj;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}