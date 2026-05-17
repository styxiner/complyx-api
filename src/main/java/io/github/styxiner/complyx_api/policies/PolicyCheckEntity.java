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

    /**
     * Parámetros del check en formato JSON.
     * Debe contener "type" + los parámetros específicos del executor del agente.
     *
     * Ejemplos:
     *   file_exists:  { "type": "file_exists", "path": "/etc/ssh/sshd_config", "file_type": "file", "mode": "0600" }
     *   file_block:   { "type": "file_block",  "path": "/etc/pam.d/common-password", "must_contain": ["minlen=15"] }
     *   file_line:    { "type": "file_line",   "path": "/etc/login.defs", "key": "PASS_MIN_LEN", "operator": ">=", "value": "15" }
     *   ini_value:    { "type": "ini_value",   "path": "/etc/security/pwquality.conf", "key": "minlen", "operator": ">=", "value": "15" }
     *   file_absent:  { "type": "file_absent", "path": "/etc/telnet.conf" }
     *   dir_contains: { "type": "dir_contains","path": "/etc/cron.d", "glob": "*.conf", "min_count": 1 }
     *   symlink:      { "type": "symlink",     "path": "/etc/localtime", "target": "/usr/share/zoneinfo/UTC" }
     *   pkg_installed:{ "type": "pkg_installed","name": "openssh-server", "version": "8.9", "operator": ">=" }
     *   pkg_absent:   { "type": "pkg_absent",  "name": "telnet" }
     *   service:      { "type": "service",     "name": "sshd", "active": true, "enabled": true }
     *   sysctl:       { "type": "sysctl",      "key": "net.ipv4.ip_forward", "operator": "=", "value": "0" }
     *   user_attr:    { "type": "user_attr",   "username": "root", "checks": [{"attr": "shell", "operator": "=", "value": "/bin/bash"}] }
     */
    @Column(name = "check_params", columnDefinition = "jsonb", nullable = false)
    private String checkParams;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_element_id", nullable = false)
    private PolicyElementEntity policyElement;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "check_regulation_sections",
        joinColumns = @JoinColumn(name = "check_id"),
        inverseJoinColumns = @JoinColumn(name = "regulation_section_id")
    )
    private Set<RegulationSectionEntity> regulationSections = new HashSet<>();

    @OneToMany(mappedBy = "policyCheck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PolicyRemediationEntity> remediations = new ArrayList<>();

    public PolicyCheckEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }

    public String getCheckParams() { return checkParams; }
    public void setCheckParams(String checkParams) { this.checkParams = checkParams; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public PolicyElementEntity getPolicyElement() { return policyElement; }
    public void setPolicyElement(PolicyElementEntity policyElement) { this.policyElement = policyElement; }

    public Set<RegulationSectionEntity> getRegulationSections() { return regulationSections; }
    public void setRegulationSections(Set<RegulationSectionEntity> regulationSections) { this.regulationSections = regulationSections; }

    public List<PolicyRemediationEntity> getRemediations() { return remediations; }
    public void setRemediations(List<PolicyRemediationEntity> remediations) { this.remediations = remediations; }

    public void addRemediation(PolicyRemediationEntity remediation) {
        remediations.add(remediation);
        remediation.setPolicyCheck(this);
    }

    public void removeRemediation(PolicyRemediationEntity remediation) {
        remediations.remove(remediation);
        remediation.setPolicyCheck(null);
    }

    public void addRegulationSection(RegulationSectionEntity section) { regulationSections.add(section); }
    public void removeRegulationSection(RegulationSectionEntity section) { regulationSections.remove(section); }

    @PrePersist
    protected void onCreate() { this.createdDate = LocalDateTime.now(); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PolicyCheckEntity that = (PolicyCheckEntity) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}