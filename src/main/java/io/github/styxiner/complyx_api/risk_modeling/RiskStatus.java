package io.github.styxiner.complyx_api.risk_modeling;

public enum RiskStatus {
    OPEN,
    ACCEPTED,
    TRANSFERRED,
    MITIGATED,
    MONITORING,
    CLOSED;

    /** Devuelve el valor que se persiste en la columna 'status' de la BD. */
    public String toDbValue() {
        return switch (this) {
            case OPEN, MITIGATED, MONITORING -> "open";
            case ACCEPTED                    -> "accepted";
            case TRANSFERRED                 -> "transferred";
            case CLOSED                      -> "closed";
        };
    }

    public boolean isClosed() {
        return this == CLOSED;
    }

    public boolean isOpen() {
        return this == OPEN || this == MITIGATED || this == MONITORING;
    }
}