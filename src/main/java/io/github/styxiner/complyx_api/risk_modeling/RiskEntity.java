package io.github.styxiner.complyx_api.risk_modeling;

import io.github.styxiner.complyx_api.agents.AgentEntity;
import io.github.styxiner.complyx_api.policies.PolicyEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "risks")
public class RiskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "threat_id", nullable = false)
    private ThreatEntity threat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private AgentEntity agent;

    public void setId(UUID id) {
		this.id = id;
	}

	public void setThreat(ThreatEntity threat) {
		this.threat = threat;
	}

	public void setAgent(AgentEntity agent) {
		this.agent = agent;
	}

	public void setImpact(BigDecimal impact) {
		this.impact = impact;
	}

	public void setProbability(BigDecimal probability) {
		this.probability = probability;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public void setStatus(RiskStatus status) {
		this.status = status;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setReviewDate(LocalDateTime reviewDate) {
		this.reviewDate = reviewDate;
	}

	public void setAcceptanceDate(LocalDateTime acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}

	public void setPolicies(Set<PolicyEntity> policies) {
		this.policies = policies;
	}

	@Column(precision = 3, scale = 1)
    private BigDecimal impact;

    @Column(precision = 3, scale = 1)
    private BigDecimal probability;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", length = 20)
    private RiskLevel riskLevel;

    // El schema usa 'status', pero el modelo de dominio tiene más estados que el schema.
    // TRANSFERRED, MITIGATED y MONITORING son estados de negocio que se mapean
    // a 'open' en BD hasta que se cierre formalmente el riesgo.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RiskStatus status;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "acceptance_date")
    private LocalDateTime acceptanceDate;

    // Relación N:M con políticas mitigadoras a través de risk_policies
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "risk_policies",
        joinColumns = @JoinColumn(name = "risk_id"),
        inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private Set<PolicyEntity> policies = new HashSet<>();

	public UUID getId() {
		return id;
	}

	public ThreatEntity getThreat() {
		return threat;
	}

	public AgentEntity getAgent() {
		return agent;
	}

	public BigDecimal getImpact() {
		return impact;
	}

	public BigDecimal getProbability() {
		return probability;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public RiskStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getReviewDate() {
		return reviewDate;
	}

	public LocalDateTime getAcceptanceDate() {
		return acceptanceDate;
	}

	public Set<PolicyEntity> getPolicies() {
		return policies;
	}

	public RiskEntity(UUID id, ThreatEntity threat, AgentEntity agent, BigDecimal impact, BigDecimal probability,
			RiskLevel riskLevel, RiskStatus status, LocalDateTime createdDate, LocalDateTime reviewDate,
			LocalDateTime acceptanceDate, Set<PolicyEntity> policies) {
		super();
		this.id = id;
		this.threat = threat;
		this.agent = agent;
		this.impact = impact;
		this.probability = probability;
		this.riskLevel = riskLevel;
		this.status = status;
		this.createdDate = createdDate;
		this.reviewDate = reviewDate;
		this.acceptanceDate = acceptanceDate;
		this.policies = policies;
	}

	public RiskEntity() {
		// TODO Auto-generated constructor stub
	}
}