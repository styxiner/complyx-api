package io.github.styxiner.complyx_api.policies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.github.styxiner.complyx_api.regulations.RegulationSectionEntity;

/*
 * Implementación manual del mapper para operaciones complejas.
 * Maneja creación y actualización profunda sin romper relaciones JPA.
 */
@Component
public class PolicyMapperImplementation {

	// ── Utilidad: JsonNode → String JSON para columnas JSONB ─────────────────
	private String toJson(com.fasterxml.jackson.databind.JsonNode node) {
		return node != null ? node.toString() : null;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// CREATE
	// ─────────────────────────────────────────────────────────────────────────

	public PolicyEntity toEntity(PolicyCreateDTO dto, Map<UUID, RegulationSectionEntity> regulationMap) {

		PolicyEntity policy = new PolicyEntity();

		policy.setName(dto.getName());
		policy.setVersion(dto.getVersion());
		policy.setDescription(dto.getDescription());
		policy.setSeverity(dto.getSeverity());
		policy.setStatus(dto.getStatus());

		if (dto.getElements() != null) {
			for (PolicyElementCreateDTO elementDTO : dto.getElements()) {

				PolicyElementEntity element = new PolicyElementEntity();
				element.setName(elementDTO.getName());

				policy.addElement(element);

				if (elementDTO.getChecks() != null) {
					for (PolicyCheckCreateDTO checkDTO : elementDTO.getChecks()) {

						PolicyCheckEntity check = new PolicyCheckEntity();

						check.setName(checkDTO.getName());
						check.setRationale(checkDTO.getRationale());
						check.setCheckParams(toJson(checkDTO.getCheckParams()));

						element.addCheck(check);

						if (checkDTO.getRemediations() != null) {
							for (PolicyRemediationCreateDTO remDTO : checkDTO.getRemediations()) {

								PolicyRemediationEntity rem = new PolicyRemediationEntity();

								rem.setName(remDTO.getName());
								rem.setDescription(remDTO.getDescription());
								rem.setRemediationParams(toJson(remDTO.getRemediationParams()));

								check.addRemediation(rem);
							}
						}

						if (checkDTO.getRegulationSectionIds() != null) {
							for (UUID id : checkDTO.getRegulationSectionIds()) {

								RegulationSectionEntity section = regulationMap.get(id);

								if (section != null) {
									check.addRegulationSection(section);
								}
							}
						}
					}
				}
			}
		}

		return policy;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// UPDATE (FULL SYNC — elimina children no presentes en el DTO)
	// ─────────────────────────────────────────────────────────────────────────

	public void updateEntity(PolicyUpdateDTO dto, PolicyEntity entity,
			Map<UUID, RegulationSectionEntity> regulationMap) {

		entity.setName(dto.getName());
		entity.setVersion(dto.getVersion());
		entity.setDescription(dto.getDescription());
		entity.setSeverity(dto.getSeverity());
		entity.setStatus(dto.getStatus());

		// ── Elementos ─────────────────────────────────────────────────────────

		Set<UUID> dtoElementIds = new HashSet<>();

		for (PolicyElementUpdateDTO elDto : dto.getElements()) {
			if (elDto.getId() != null) {
				dtoElementIds.add(elDto.getId());
			}
		}

		Iterator<PolicyElementEntity> elementIterator = entity.getElements().iterator();

		while (elementIterator.hasNext()) {

			PolicyElementEntity element = elementIterator.next();

			if (element.getId() != null && !dtoElementIds.contains(element.getId())) {
				elementIterator.remove();
			}
		}

		Map<UUID, PolicyElementEntity> existingElements = new HashMap<>();

		for (PolicyElementEntity e : entity.getElements()) {
			if (e.getId() != null) {
				existingElements.put(e.getId(), e);
			}
		}

		for (PolicyElementUpdateDTO elementDTO : dto.getElements()) {

			PolicyElementEntity element;

			if (elementDTO.getId() != null && existingElements.containsKey(elementDTO.getId())) {
				element = existingElements.get(elementDTO.getId());
				element.setName(elementDTO.getName());

			} else {

				element = new PolicyElementEntity();

				element.setName(elementDTO.getName());

				entity.addElement(element);
			}

			// ── Checks ────────────────────────────────────────────────────────

			Set<UUID> dtoCheckIds = new HashSet<>();

			for (PolicyCheckUpdateDTO chkDto : elementDTO.getChecks()) {
				if (chkDto.getId() != null) {
					dtoCheckIds.add(chkDto.getId());
				}
			}

			Iterator<PolicyCheckEntity> checkIterator = element.getChecks().iterator();

			while (checkIterator.hasNext()) {

				PolicyCheckEntity check = checkIterator.next();

				if (check.getId() != null && !dtoCheckIds.contains(check.getId())) {

					checkIterator.remove();
				}
			}

			Map<UUID, PolicyCheckEntity> existingChecks = new HashMap<>();

			for (PolicyCheckEntity c : element.getChecks()) {
				if (c.getId() != null) {
					existingChecks.put(c.getId(), c);
				}
			}

			for (PolicyCheckUpdateDTO checkDTO : elementDTO.getChecks()) {

				PolicyCheckEntity check;

				if (checkDTO.getId() != null && existingChecks.containsKey(checkDTO.getId())) {

					check = existingChecks.get(checkDTO.getId());

				} else {

					check = new PolicyCheckEntity();

					element.addCheck(check);
				}

				check.setName(checkDTO.getName());
				check.setRationale(checkDTO.getRationale());
				check.setCheckParams(toJson(checkDTO.getCheckParams()));

				// ── Remediations ──────────────────────────────────────────────

				if (checkDTO.getRemediations() != null) {

					Set<UUID> dtoRemIds = new HashSet<>();

					for (PolicyRemediationUpdateDTO remDto : checkDTO.getRemediations()) {

						if (remDto.getId() != null) {
							dtoRemIds.add(remDto.getId());
						}
					}

					Iterator<PolicyRemediationEntity> remIterator = check.getRemediations().iterator();

					while (remIterator.hasNext()) {

						PolicyRemediationEntity rem = remIterator.next();

						if (rem.getId() != null && !dtoRemIds.contains(rem.getId())) {

							remIterator.remove();
						}
					}

					Map<UUID, PolicyRemediationEntity> existingRem = new HashMap<>();

					for (PolicyRemediationEntity r : check.getRemediations()) {

						if (r.getId() != null) {
							existingRem.put(r.getId(), r);
						}
					}

					for (PolicyRemediationUpdateDTO remDTO : checkDTO.getRemediations()) {

						PolicyRemediationEntity rem;

						if (remDTO.getId() != null && existingRem.containsKey(remDTO.getId())) {

							rem = existingRem.get(remDTO.getId());

						} else {

							rem = new PolicyRemediationEntity();

							check.addRemediation(rem);
						}

						rem.setName(remDTO.getName());
						rem.setDescription(remDTO.getDescription());
						rem.setRemediationParams(toJson(remDTO.getRemediationParams()));
					}

				} else {

					check.getRemediations().clear();
				}

				// ── Regulation sections (replace) ─────────────────────────────

				if (checkDTO.getRegulationSectionIds() != null) {

					check.getRegulationSections().clear();

					for (UUID id : checkDTO.getRegulationSectionIds()) {

						RegulationSectionEntity section = regulationMap.get(id);

						if (section != null) {
							check.addRegulationSection(section);
						}
					}
				}
			}
		}
	}
}