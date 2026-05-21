package io.github.styxiner.complyx_api.risk_modeling;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte RiskStatus ↔ String para que Hibernate guarde en minúsculas.
 *
 * BD:    'open', 'accepted', 'transferred', 'closed'
 * Enum:  OPEN,   ACCEPTED,   TRANSFERRED,   CLOSED,  ...
 *
 * Nota: MONITORING y MITIGATED no existen en el constraint de BD —
 * el servicio los mapea a 'open' antes de persistir, pero el converter
 * los guarda como 'monitoring'/'mitigated' si el constraint lo permitiera.
 * Actualmente el servicio gestiona esto explícitamente.
 */
@Converter
public class RiskStatusConverter implements AttributeConverter<RiskStatus, String> {

    @Override
    public String convertToDatabaseColumn(RiskStatus status) {
        if (status == null) return null;
        return status.name().toLowerCase();
    }

    @Override
    public RiskStatus convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return RiskStatus.valueOf(dbValue.toUpperCase());
    }
}