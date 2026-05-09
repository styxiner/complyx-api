package io.github.styxiner.complyx_api.policies;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/*
 * Repositorio de acceso a datos para PolicyEntity.
 * Combina:JpaRepository (CRUD básico) y
 * JpaSpecificationExecutor(soporte para filtros dinámicos (Specifications)
 */
public interface PolicyRepository extends JpaRepository<PolicyEntity, UUID>, JpaSpecificationExecutor<PolicyEntity> {
	// Busca una politica por nombre exacto.
	Optional<PolicyEntity> findByName(String name);

	// Verifica si existe una politica con ese nombre.
	// (más eficiente que findByName cuando solo se valida existencia)
	boolean existsByName(String name);
}
