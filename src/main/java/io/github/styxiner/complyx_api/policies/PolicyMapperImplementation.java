package io.github.styxiner.complyx_api.policies;


import java.util.*;

import org.springframework.stereotype.Component;

import io.github.styxiner.complyx_api.regulations.RegulationSectionEntity;

/*
 * Implementación manual del mapper para operaciones complejas.
 * Maneja creación y actualización profunda sin romper relaciones JPA.
 */
@Component
public class PolicyMapperImplementation {

	/*
	 * Construye una Politica completa desde DTO. Usa helpers de entidad para
	 * mantener consistencia bidireccional.
	 */
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
						check.setCheckCommand(checkDTO.getCheckCommand());
						check.setRationale(checkDTO.getRationale());

						element.addCheck(check);

						// Remediations
						if (checkDTO.getRemediations() != null) {
							for (PolicyRemediationCreateDTO remDTO : checkDTO.getRemediations()) {

								PolicyRemediationEntity rem = new PolicyRemediationEntity();
								rem.setName(remDTO.getName());
								rem.setDescription(remDTO.getDescription());
								rem.setRemediationCommand(remDTO.getRemediationCommand());

								check.addRemediation(rem);
							}
						}

						// Regulation sections
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

	/*
	 * Estrategia de actualizacion utilizada - IGNORE missing children, no
	 * eliminacion -solo actualizar o crear nuevas entidades
	 *
	 * estrategia alternativa - FULL SYNC eliminiar los "missing children" -
	 * requeriría usar removeXXX() + orphanRemoval
	 */
	public void updateEntity(PolicyUpdateDTO dto, PolicyEntity entity,
			Map<UUID, RegulationSectionEntity> regulationMap) {

		entity.setName(dto.getName());
		entity.setVersion(dto.getVersion());
		entity.setDescription(dto.getDescription());
		entity.setSeverity(dto.getSeverity());
		entity.setStatus(dto.getStatus());

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

			// CHECKS
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
					check.setName(checkDTO.getName());
					check.setCheckCommand(checkDTO.getCheckCommand());
					check.setRationale(checkDTO.getRationale());
				} else {
					check = new PolicyCheckEntity();
					check.setName(checkDTO.getName());
					check.setCheckCommand(checkDTO.getCheckCommand());
					check.setRationale(checkDTO.getRationale());
					element.addCheck(check);
				}

				// REMEDIATIONS
				Map<UUID, PolicyRemediationEntity> existingRem = new HashMap<>();

				for (PolicyRemediationEntity r : check.getRemediations()) {
					if (r.getId() != null) {
						existingRem.put(r.getId(), r);
					}
				}

				if (checkDTO.getRemediations() != null) {
					for (PolicyRemediationUpdateDTO remDTO : checkDTO.getRemediations()) {

						PolicyRemediationEntity rem;

						if (remDTO.getId() != null && existingRem.containsKey(remDTO.getId())) {
							rem = existingRem.get(remDTO.getId());
							rem.setName(remDTO.getName());
							rem.setDescription(remDTO.getDescription());
							rem.setRemediationCommand(remDTO.getRemediationCommand());
						} else {
							rem = new PolicyRemediationEntity();
							rem.setName(remDTO.getName());
							rem.setDescription(remDTO.getDescription());
							rem.setRemediationCommand(remDTO.getRemediationCommand());
							check.addRemediation(rem);
						}
					}
				}

				// REGULATION SECTIONS (REPLACE CONTROLADO)
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