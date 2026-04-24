package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

/*
 * Clase que construye filtros dinámicos para AgentEntity.
 * Cada método representa una condición opcional.
 * Si el valor es null, no se aplica filtro.
 */
public class AgentSpecifications {
	public static Specification<AgentEntity> hasIp(String ip) {
		return (root, query, cb) -> // root=tabla,cb=CriteriaBuilder
		ip == null ? null : cb.equal(root.get("ip"), ip); // Busqueda por IP exacta
	}

	public static Specification<AgentEntity> hasHostname(String hostname) {
		return (root, query, cb) -> hostname == null ? null
				: cb.like(cb.lower(root.get("hostname")), "%" + hostname.toLowerCase() + "%");
	}

	public static Specification<AgentEntity> hasOsName(String nombre) {
		return (root, query, cb) -> nombre == null ? null
				: cb.like(cb.lower(root.get("osName")), "%" + nombre.toLowerCase() + "%");
	}

	// la clase envoltorio Boolean permite null para aplicar el filtro solo cuando
	// sea necesaario
	public static Specification<AgentEntity> isEnabled(Boolean enabled) {
		return (root, query, cb) -> enabled == null ? null : cb.equal(root.get("enabled"), enabled);
	}

	/*
	 * Al usar root.join, si un agente pertenece a 3 grupos diferentes podría haber
	 * duplicados en el listado. añadir query.distinct(true); dentro de la
	 * Specification si esto sucede.
	 */
	public static Specification<AgentEntity> inGroup(UUID groupId) {
		return (root, query, cb) -> {
			if (groupId == null)
				return null;

			// JOIN con la tabla de grupos (ManyToMany)
			Join<Object, Object> groupJoin = root.join("groups", JoinType.INNER);
			/*
			 * usa la relacion definida en el atributo groups de la entidad para "unirse" a
			 * la tabla grupos, con JoinType.Inner conseguimos agentes que sí tengan al
			 * menos un grupo asignado que coincida
			 */
			return cb.equal(groupJoin.get("id"), groupId);
			// para buscar en la tabla de grupos aquel cuyo id sea igual al gruopId del
			// parámetro
		};
	}

	/*
	 * Construye la Specification final combinando todos los filtros.
	 * Specification.where(spec) está obsoleto, con Specification.unrestricted() se
	 * empieza sin restricciones y se van añadiendo condiciones
	 */
	public static Specification<AgentEntity> build(AgentFilter filter) {

		if (filter == null) {
			return Specification.unrestricted();
		}

		Specification<AgentEntity> spec = Specification.unrestricted();

		if (filter.getIp() != null) {
			spec = spec.and(hasIp(filter.getIp()));
		}

		if (filter.getHostname() != null) {
			spec = spec.and(hasHostname(filter.getHostname()));
		}

		if (filter.getOsName() != null) {
			spec = spec.and(hasOsName(filter.getOsName()));
		}

		if (filter.getEnabled() != null) {
			spec = spec.and(isEnabled(filter.getEnabled()));
		}

		if (filter.getGroupId() != null) {
			spec = spec.and(inGroup(filter.getGroupId()));
		}
		return spec;
	}
}
