package io.github.styxiner.complyx_api.policies;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Mapper MapStruct para Entity → DTO de lectura (listados y detalle).
 * La conversión String JSONB ↔ JsonNode se gestiona con los métodos helper.
 */
@Mapper(componentModel = "spring")
public interface PolicyMapper {

    @Mapping(target = "status",    source = "status")
    @Mapping(target = "createdAt", source = "createdDate")
    PolicySummaryDTO toSummaryDTO(PolicyEntity entity);

    @Mapping(target = "status",    source = "status")
    @Mapping(target = "createdAt", source = "createdDate")
    PolicyDetailDTO toDetailDTO(PolicyEntity entity);

    List<PolicyDetailDTO>  toDetailDTOList(List<PolicyEntity> entities);
    List<PolicySummaryDTO> toSummaryList(List<PolicyEntity> entities);

    // ── Conversiones String JSONB ↔ JsonNode para MapStruct ──────────────────
    // MapStruct llama automáticamente a estos métodos cuando necesita convertir
    // entre String (columna JSONB en la entidad) y JsonNode (campo del DTO).

    default JsonNode stringToJsonNode(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON inválido en columna JSONB: " + json, e);
        }
    }

    default String jsonNodeToString(JsonNode node) {
        return node != null ? node.toString() : null;
    }
}