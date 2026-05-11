package io.github.styxiner.complyx_api.risk_modeling;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "threats")
public class ThreatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(updatable = false, nullable = false)
	private UUID id;

	@Column(nullable = false, unique = true, length = 100)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(length = 50)
	private String category;

	@Column(name = "severity_score", precision = 3, scale = 1)
	private BigDecimal severityScore;

	@CreationTimestamp
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	public void setId(UUID id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setSeverityScore(BigDecimal severityScore) {
		this.severityScore = severityScore;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public ThreatEntity(UUID id, String name, String description, String category, BigDecimal severityScore,
			LocalDateTime createdDate) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.severityScore = severityScore;
		this.createdDate = createdDate;
	}

	public ThreatEntity() {		
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public BigDecimal getSeverityScore() {
		return severityScore;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

}