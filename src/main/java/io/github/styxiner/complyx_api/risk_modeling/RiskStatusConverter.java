package io.github.styxiner.complyx_api.risk_modeling;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte RiskStatus ↔ String para la columna status.
 * El schema PostgreSQL acepta: 'open', 'accepted', 'transferred', 'closed'.
 * Los estados MITIGATED y MONITORING se mapean a 'open' en BD.
 *
 * Al leer de BD, 'open' se convierte a OPEN (el estado MITIGATED/MONITORING
 * se gestiona a nivel de aplicación, no se persiste en la columna status).
 */
@Converter(autoApply = true)
public class RiskStatusConverter implements AttributeConverter<RiskStatus, String> {

    @Override
    public String convertToDatabaseColumn(RiskStatus status) {
        return status == null ? null : status.toDbValue();
    }

    @Override
    public RiskStatus convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "open"        -> RiskStatus.OPEN;
            case "accepted"    -> RiskStatus.ACCEPTED;
            case "transferred" -> RiskStatus.TRANSFERRED;
            case "closed"      -> RiskStatus.CLOSED;
            default -> throw new IllegalArgumentException("Estado de riesgo desconocido: " + dbValue);
        };
    }
}