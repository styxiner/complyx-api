package io.github.styxiner.complyx_api.policies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Cumplimiento de una política para un agente concreto. Jerarquía: política →
 * elementos → checks (con último resultado).
 */
public class PolicyComplianceDTO {

	private UUID policyId;
	private String policyName;
	private String policyVersion;
	private String severity;

	/** Score global = media ponderada de los scores de los elementos */
	private double globalScore;
	private int totalChecks;
	private int passedChecks;

	private List<ElementComplianceDTO> elements;

	// ── Elemento ─────────────────────────────────────────────────────────────

	public static class ElementComplianceDTO {
		private UUID elementId;
		private String elementName;
		private int totalChecks;
		private int passedChecks;
		private double score;
		private LocalDateTime lastUpdated;
		private List<CheckComplianceDTO> checks;

		public UUID getElementId() {
			return elementId;
		}

		public String getElementName() {
			return elementName;
		}

		public int getTotalChecks() {
			return totalChecks;
		}

		public int getPassedChecks() {
			return passedChecks;
		}

		public double getScore() {
			return score;
		}

		public LocalDateTime getLastUpdated() {
			return lastUpdated;
		}

		public List<CheckComplianceDTO> getChecks() {
			return checks;
		}

		public void setElementId(UUID v) {
			this.elementId = v;
		}

		public void setElementName(String v) {
			this.elementName = v;
		}

		public void setTotalChecks(int v) {
			this.totalChecks = v;
		}

		public void setPassedChecks(int v) {
			this.passedChecks = v;
		}

		public void setScore(double v) {
			this.score = v;
		}

		public void setLastUpdated(LocalDateTime v) {
			this.lastUpdated = v;
		}

		public void setChecks(List<CheckComplianceDTO> v) {
			this.checks = v;
		}
	}

	// ── Check con su último resultado ─────────────────────────────────────────

	public static class CheckComplianceDTO {
		private UUID checkId;
		private String checkName;
		private String rationale;

		/** null = sin resultados aún */
		private Boolean passed;
		private String detail;
		private String actualValue;
		private String expectedValue;
		private LocalDateTime executedAt;

		public UUID getCheckId() {
			return checkId;
		}

		public String getCheckName() {
			return checkName;
		}

		public String getRationale() {
			return rationale;
		}

		public Boolean getPassed() {
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

		public void setCheckId(UUID v) {
			this.checkId = v;
		}

		public void setCheckName(String v) {
			this.checkName = v;
		}

		public void setRationale(String v) {
			this.rationale = v;
		}

		public void setPassed(Boolean v) {
			this.passed = v;
		}

		public void setDetail(String v) {
			this.detail = v;
		}

		public void setActualValue(String v) {
			this.actualValue = v;
		}

		public void setExpectedValue(String v) {
			this.expectedValue = v;
		}

		public void setExecutedAt(LocalDateTime v) {
			this.executedAt = v;
		}
	}

	// ── Getters/setters raíz ──────────────────────────────────────────────────

	public UUID getPolicyId() {
		return policyId;
	}

	public String getPolicyName() {
		return policyName;
	}

	public String getPolicyVersion() {
		return policyVersion;
	}

	public String getSeverity() {
		return severity;
	}

	public double getGlobalScore() {
		return globalScore;
	}

	public int getTotalChecks() {
		return totalChecks;
	}

	public int getPassedChecks() {
		return passedChecks;
	}

	public List<ElementComplianceDTO> getElements() {
		return elements;
	}

	public void setPolicyId(UUID v) {
		this.policyId = v;
	}

	public void setPolicyName(String v) {
		this.policyName = v;
	}

	public void setPolicyVersion(String v) {
		this.policyVersion = v;
	}

	public void setSeverity(String v) {
		this.severity = v;
	}

	public void setGlobalScore(double v) {
		this.globalScore = v;
	}

	public void setTotalChecks(int v) {
		this.totalChecks = v;
	}

	public void setPassedChecks(int v) {
		this.passedChecks = v;
	}

	public void setElements(List<ElementComplianceDTO> v) {
		this.elements = v;
	}
}