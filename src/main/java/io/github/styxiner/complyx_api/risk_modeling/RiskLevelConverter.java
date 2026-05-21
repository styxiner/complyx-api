package io.github.styxiner.complyx_api.risk_modeling;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte RiskLevel ↔ String para que Hibernate guarde en minúsculas
 * (lo que exige el constraint de PostgreSQL) y lea de vuelta correctamente.
 *
 * BD:    'low', 'medium', 'high', 'critical'
 * Enum:  LOW,   MEDIUM,   HIGH,   CRITICAL
 */
@Converter
public class RiskLevelConverter implements AttributeConverter<RiskLevel, String> {

    @Override
    public String convertToDatabaseColumn(RiskLevel level) {
        if (level == null) return null;
        return level.name().toLowerCase();
    }

    @Override
    public RiskLevel convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return RiskLevel.valueOf(dbValue.toUpperCase());
    }
}