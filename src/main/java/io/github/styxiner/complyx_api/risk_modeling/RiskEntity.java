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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private Set<PolicyEntity> policies = new HashSet<>();
}