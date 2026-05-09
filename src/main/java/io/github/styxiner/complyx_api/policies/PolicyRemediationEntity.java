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
	    @Column(name = "remediation_command")
	    private String remediationCommand;
	    @Column(name = "created_date", nullable = false, updatable = false)
	    private LocalDateTime createdDate;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "policy_check_id", nullable = false)
	    private PolicyCheckEntity policyCheck;

	    public PolicyRemediationEntity() {}

	    public PolicyRemediationEntity(String name, String description, String remediationCommand) {
	        this.name = name;
	        this.description = description;
	        this.remediationCommand = remediationCommand;
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

		public String getRemediationCommand() {
			return remediationCommand;
		}

		public void setRemediationCommand(String remediationCommand) {
			this.remediationCommand = remediationCommand;
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
		/*
	     * Callback de JPA ejecutado antes de INSERT.
	     * Garantiza que la fecha de creación se establezca automáticamente
	     */
	    @PrePersist
	    protected void onCreate() {
	        this.createdDate = LocalDateTime.now();
	    }

		@Override
		public int hashCode() {
			return getClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
		    if (this == obj) return true;
		    if (obj == null || getClass() != obj.getClass()) return false;
		    PolicyRemediationEntity that = (PolicyRemediationEntity) obj;
		    return id != null && id.equals(that.id);
		}




	    }
