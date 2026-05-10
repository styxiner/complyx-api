package io.github.styxiner.complyx_api.risk_modeling;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte RiskLevel ↔ String lowercase para la columna risk_level.
 * El schema PostgreSQL tiene CHECK (risk_level IN ('low','medium','high','critical')).
 */
@Converter(autoApply = true)
public class RiskLevelConverter implements AttributeConverter<RiskLevel, String> {

    @Override
    public String convertToDatabaseColumn(RiskLevel level) {
        return level == null ? null : level.toDbValue();
    }

    @Override
    public RiskLevel convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return RiskLevel.valueOf(dbValue.toUpperCase());
    }
}