package io.github.styxiner.complyx_api.agents;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.styxiner.complyx_api.policies.PolicyCheckEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "check_results")
public class CheckResultEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private AgentEntity agent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "check_id", nullable = false)
	private PolicyCheckEntity check;

	@Column(nullable = false)
	private boolean passed;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String detail;

	@Column(name = "actual_value")
	private String actualValue;

	@Column(name = "expected_value")
	private String expectedValue;

	@Column(name = "executed_at", nullable = false)
	private LocalDateTime executedAt;

	@Column(name = "received_at", nullable = false)
	private LocalDateTime receivedAt;

	public UUID getId() {
		return id;
	}

	public AgentEntity getAgent() {
		return agent;
	}

	public PolicyCheckEntity getCheck() {
		return check;
	}

	public boolean isPassed() {
		return passed;
	}

	public String getDetail() {
		return detail;
	}

	public String getActualValue() {
		return actualValue;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public LocalDateTime getExecutedAt() {
		return executedAt;
	}

	public LocalDateTime getReceivedAt() {
		return receivedAt;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setAgent(AgentEntity agent) {
		this.agent = agent;
	}

	public void setCheck(PolicyCheckEntity check) {
		this.check = check;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setActualValue(String v) {
		this.actualValue = v;
	}

	public void setExpectedValue(String v) {
		this.expectedValue = v;
	}

	public void setExecutedAt(LocalDateTime t) {
		this.executedAt = t;
	}

	public void setReceivedAt(LocalDateTime t) {
		this.receivedAt = t;
	}
}