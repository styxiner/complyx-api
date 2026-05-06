package io.github.styxiner.complyx_api.policies;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado operativo actual de la política")
public enum PolicyStatus {
    @Schema(description = "En edición, no aplicable aún")
    DRAFT,
    @Schema(description = "Aplicándose actualmente a los agentes")
    ACTIVE,
    @Schema(description = "Deshabilitada temporalmente")
    INACTIVE,
    @Schema(description = "Obsoleta pero guardada por temas de auditoría")
    ARCHIVED
}