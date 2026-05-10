package io.github.styxiner.complyx_api.risk_modeling;

/**
 * Nivel de riesgo calculado a partir de impacto × probabilidad.
 *
 * El schema PostgreSQL usa lowercase: 'low', 'medium', 'high', 'critical'.
 * JPA persiste el nombre del enum, así que se necesita un converter o
 * usar @Column con columnDefinition si el driver es estricto.
 *
 * Matriz de cálculo (ver RiskService.computeRiskLevel):
 *   producto < 20  → LOW
 *   20 ≤ p < 49    → MEDIUM
 *   49 ≤ p < 70    → HIGH
 *   p ≥ 70         → CRITICAL
 */
public enum RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    /** Valor lowercase para persistir en la columna risk_level. */
    public String toDbValue() {
        return name().toLowerCase();
    }

    public static RiskLevel fromScore(double impact, double probability) {
        double product = impact * probability;
        if (product >= 70) return CRITICAL;
        if (product >= 49) return HIGH;
        if (product >= 20) return MEDIUM;
        return LOW;
    }
}