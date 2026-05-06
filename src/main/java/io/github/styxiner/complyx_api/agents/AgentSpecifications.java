package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.*;
/*
 * Clase que construye filtros din�micos para AgentEntity.
 * Cada m�todo representa una condici�n opcional.
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
	 * Al usar root.join, si un agente pertenece a 3 grupos diferentes podr�a haber
	 * duplicados en el listado. a�adir query.distinct(true); dentro de la
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
			 * la tabla grupos, con JoinType.Inner conseguimos agentes que s� tengan al
			 * menos un grupo asignado que coincida
			 */
			return cb.equal(groupJoin.get("id"), groupId);
			// para buscar en la tabla de grupos aquel cuyo id sea igual al gruopId del
			// par�metro
		};


/*
 * Clase que construye filtros din�micos para AgentEntity.
 * Cada m�todo representa una condici�n opcional.
 * Si el valor es null, no se aplica filtro.
 */
@Schema(description = "Filtros din�micos para b�squeda de agentes")
public class AgentSpecifications {
// * Filtra agentes por IP exacta.Si es null, no aplica ning�n filtro.	
	public static Specification<AgentEntity> hasIp(String ip) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (ip == null) {
					return cb.conjunction();
				}
				return cb.equal(root.get("ip"), ip);
			}
		};
	}

//Filtra por hostname usando b�squeda parcial (LIKE, case-insensitive).
	public static Specification<AgentEntity> hasHostname(String hostname) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (hostname == null) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("hostname")), "%" + hostname.toLowerCase() + "%");
			}
		};
	}

// Filtra por nombre del sistema operativo (LIKE, case-insensitive).
	public static Specification<AgentEntity> hasOsName(String osName) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (osName == null) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("osName")), "%" + osName.toLowerCase() + "%");
			}
		};
	}

	// Filtra por estado habilitado/deshabilitado. la clase envoltorio Boolean
	// permite null para aplicar el filtro solo cuando
	// sea necesaario
	public static Specification<AgentEntity> isEnabled(Boolean enabled) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (enabled == null) {
					return cb.conjunction();
				}
				return cb.equal(root.get("enabled"), enabled);
			}
		};
	}

	/*
	 * * Filtra agentes que pertenecen a un grupo espec�fico. Usa JOIN ManyToMany y
	 * distinct para evitar duplicados.
	 */
	public static Specification<AgentEntity> inGroup(UUID groupId) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (groupId == null) {
					return cb.conjunction();
				}
				query.distinct(true);
				/*
				 * usa la relacion definida en el atributo groups de la entidad para "unirse" a
				 * la tabla grupos, con JoinType.Inner conseguimos agentes que s� tengan al
				 * menos un grupo asignado que coincida
				 */
				Join<Object, Object> groupJoin = root.join("groups", JoinType.INNER);
				// para buscar en la tabla de grupos aquel cuyo id sea igual al gruopId del
				// par�metro
				return cb.equal(groupJoin.get("id"), groupId);
			}
		};

	}

	/*
	 * Construye la Specification final combinando todos los filtros.
	 * Specification.where(spec) est� obsoleto, con Specification.unrestricted() se
	 * empieza sin restricciones y se van a�adiendo condiciones
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
