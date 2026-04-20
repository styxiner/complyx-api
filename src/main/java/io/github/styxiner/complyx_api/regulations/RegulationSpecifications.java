package io.github.styxiner.complyx_api.regulations;

import org.springframework.data.jpa.domain.Specification;

public class RegulationSpecifications {
	Specification<RegulationEntity> hasName(String name) {return null;}
	Specification<RegulationEntity> hasSectionTitle(String title) {return null;}
	Specification<RegulationEntity> build(RegulationFilter filter) {return null;}
}
