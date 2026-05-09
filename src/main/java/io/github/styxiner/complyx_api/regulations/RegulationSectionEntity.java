package io.github.styxiner.complyx_api.regulations;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import io.github.styxiner.complyx_api.policies.PolicyCheckEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "regulation_sections")
public class RegulationSectionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulation_id", nullable = false)
    private RegulationEntity regulation;

    @ManyToMany(mappedBy = "regulationSections")
    private Set<PolicyCheckEntity> checks = new HashSet<>();

    public RegulationSectionEntity() {}

    public RegulationSectionEntity(String title, RegulationEntity regulation) {
        this.title = title; this.regulation = regulation;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public RegulationEntity getRegulation() { return regulation; }
    public void setRegulation(RegulationEntity regulation) { this.regulation = regulation; }
    public Set<PolicyCheckEntity> getChecks() { return checks; }
    public void setChecks(Set<PolicyCheckEntity> checks) { this.checks = checks; }
}