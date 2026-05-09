package io.github.styxiner.complyx_api.policies;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/*
import io.github.styxiner.complyx_api.regulations.RegulationSectionEntity;
import io.github.styxiner.complyx_api.regulations.RegulationSectionRepository;
*/
import io.github.styxiner.complyx_api.agents.AgentEntity;
import io.github.styxiner.complyx_api.agents.AgentGroupEntity;
import io.github.styxiner.complyx_api.agents.AgentGroupRepository;
import io.github.styxiner.complyx_api.agents.AgentRepository;

/*
 * Servicio principal de gestión de Policies.
 *
 * RESPONSABILIDAD:
 * - Validar datos de entrada
 * - Orquestar lógica de dominio
 * - Delegar mapping sin romper JPA
 *
 * NOTA:
 * La gestión de RegulationSection está preparada pero comentada
 * hasta que el módulo correspondiente esté disponible.
 */
@Service
@Transactional
public class PolicyServiceImplementation implements PolicyService {

	private final PolicyRepository policyRepository;
	private final PolicyMapper policyMapper;
	private final PolicyMapperImplementation policyMapperImplementation;
	private final AgentRepository agentRepository;
	private final AgentGroupRepository agentGroupRepository;
// FUTURO: integración con regulation
	/* private final RegulationSectionRepository regulationRepository; */

	public PolicyServiceImplementation(PolicyRepository policyRepository, PolicyMapper policyMapper,
			PolicyMapperImplementation policyMapperImplementation, AgentRepository agentRepository,
			AgentGroupRepository agentGroupRepository) {
		/* RegulationSectionRepository regulationRepository */
		// this.regulationRepository = regulationRepositor
		super();
		this.policyRepository = policyRepository;
		this.policyMapper = policyMapper;
		this.policyMapperImplementation = policyMapperImplementation;
		this.agentRepository = agentRepository;
		this.agentGroupRepository = agentGroupRepository;
	}

	// READ
	@Override
	@Transactional(readOnly = true)
	public PolicyDetailDTO getPolicyById(UUID id) {

		PolicyEntity entity = policyRepository.findById(id)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		return policyMapper.toDetailDTO(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PolicySummaryDTO> getPoliciesByAgent(UUID agentId) {

		List<PolicyEntity> policies = policyRepository.findAll(PolicySpecifications.assignedToAgent(agentId));

		return policyMapper.toSummaryList(policies);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PolicySummaryDTO> getAllPolicies(PolicyFilter filter, Pageable pageable) {

		Page<PolicyEntity> page = policyRepository.findAll(PolicySpecifications.build(filter), pageable);

		return page.map(new java.util.function.Function<PolicyEntity, PolicySummaryDTO>() {
			@Override
			public PolicySummaryDTO apply(PolicyEntity entity) {
				return policyMapper.toSummaryDTO(entity);
			}
		});
	}

	// CREATE
	@Override
	public PolicyDetailDTO createPolicy(PolicyCreateDTO dto) {

		validateCreate(dto);

		// FUTURO: cargar regulation sections
		/*
		 * Map<UUID, RegulationSectionEntity> regulationMap =
		 * resolveRegulationsFromCreate(dto);
		 */

		PolicyEntity entity = policyMapperImplementation.toEntity(dto, new HashMap<>() // vacío temporal FUTURO cambiar por
		// regulationMap
		);

		PolicyEntity saved = policyRepository.save(entity);

		return policyMapper.toDetailDTO(saved);
	}

	// UPDATE
	@Override
	public PolicyDetailDTO updatePolicy(UUID id, PolicyUpdateDTO dto) {

		PolicyEntity entity = policyRepository.findById(id)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		validateUpdate(dto);

		// FUTURO: cargar regulation sections
		/*
		 * Map<UUID, RegulationSectionEntity> regulationMap =
		 * resolveRegulationsFromUpdate(dto);
		 */

		policyMapperImplementation.updateEntity(dto, entity, new HashMap<>()
		// regulationMap
		);

		PolicyEntity saved = policyRepository.save(entity);

		return policyMapper.toDetailDTO(saved);
	}

	// DELETE
	@Override
	public void deletePolicy(UUID policyId) {

		PolicyEntity policy = policyRepository.findById(policyId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		/*
		 * IMPORTANTE: ManyToMany no siempre se limpia automáticamente de forma
		 * explícita en memoria, así que rompemos relaciones para evitar
		 * inconsistencias.
		 */

		policy.getAgents().clear();
		policy.getGroups().clear();

		/*
		 * CascadeType.ALL + orphanRemoval=true hacen el trabajo pesado: - elements -
		 * checks - remediations
		 */

		policyRepository.delete(policy);
	}


//ASIGNACIONES
	@Override
	public void assignToAgent(UUID policyId, UUID agentId) {

		PolicyEntity policy = policyRepository.findById(policyId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Agent not found");
					}
				});

		/*
		 * Evita duplicados en memoria y en BD.
		 */
		if (!policy.getAgents().contains(agent)) {
			policy.addAgent(agent);
		}

		policyRepository.save(policy);
	}

	@Override
	public void unAssignToAgent(UUID policyId, UUID agentId) {

		PolicyEntity policy = policyRepository.findById(policyId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Agent not found");
					}
				});

		if (policy.getAgents().contains(agent)) {
			policy.removeAgent(agent);
		}

		policyRepository.save(policy);
	}

	@Override
	public void assignToGroup(UUID policyId, UUID groupId) {

		PolicyEntity policy = policyRepository.findById(policyId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		AgentGroupEntity group = agentGroupRepository.findById(groupId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Agent group not found");
					}
				});

		/*
		 * Evita duplicados en la relación ManyToMany.
		 */
		if (!policy.getGroups().contains(group)) {
			policy.addGroup(group);
		}

		policyRepository.save(policy);
	}

	@Override
	public void unAssignToGroup(UUID policyId, UUID groupId) {

		PolicyEntity policy = policyRepository.findById(policyId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Policy not found");
					}
				});

		AgentGroupEntity group = agentGroupRepository.findById(groupId)
				.orElseThrow(new java.util.function.Supplier<RuntimeException>() {
					@Override
					public RuntimeException get() {
						return new RuntimeException("Agent group not found");
					}
				});

		if (policy.getGroups().contains(group)) {
			policy.removeGroup(group);
		}

		policyRepository.save(policy);
	}

	// REGULACION (FUTURO)

	/*
	 * activar cuando el módulo de regulations esté disponible
	 *
	 * Extrae IDs desde DTO y carga entidades desde DB.
	 */
	/*
	 * private Map<UUID, RegulationSectionEntity>
	 * resolveRegulationsFromCreate(PolicyCreateDTO dto) {
	 * 
	 * Set<UUID> ids = new HashSet<>();
	 * 
	 * for (PolicyElementCreateDTO element : dto.getElements()) { for
	 * (PolicyCheckCreateDTO check : element.getChecks()) {
	 * 
	 * if (check.getRegulationSectionIds() != null) {
	 * ids.addAll(check.getRegulationSectionIds()); } } }
	 * 
	 * return loadRegulations(ids); }
	 */

	/*
	 * private Map<UUID, RegulationSectionEntity>
	 * resolveRegulationsFromUpdate(PolicyUpdateDTO dto) {
	 * 
	 * Set<UUID> ids = new HashSet<>();
	 * 
	 * for (PolicyElementUpdateDTO element : dto.getElements()) { for
	 * (PolicyCheckUpdateDTO check : element.getChecks()) {
	 * 
	 * if (check.getRegulationSectionIds() != null) {
	 * ids.addAll(check.getRegulationSectionIds()); } } }
	 * 
	 * return loadRegulations(ids); }
	 */

	/*
	 *private Map<UUID, RegulationSectionEntity> loadRegulations(Set<UUID> ids) {
		Map<UUID, RegulationSectionEntity> regulationMap = new HashMap<>();

		if (ids == null || ids.isEmpty()) {
			return regulationMap;
		}

		List<RegulationSectionEntity> sections = regulationRepository.findAllById(ids);

		for (RegulationSectionEntity section : sections) {
			regulationMap.put(section.getId(), section);
		}

		// Validación importante: si faltan IDs, devolvemos error y no dejamos referencias rotas.
		if (regulationMap.size() != ids.size()) {
			throw new IllegalArgumentException("One or more regulation sections do not exist");
		}

		return regulationMap;
	}
	 */
}