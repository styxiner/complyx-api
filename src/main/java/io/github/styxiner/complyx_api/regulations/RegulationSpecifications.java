package io.github.styxiner.complyx_api.regulations;

import org.springframework.data.jpa.domain.Specification;

public class RegulationSpecifications {

    public static Specification<RegulationEntity> hasName(String name) {
        return (root, query, cb) ->
            name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<RegulationEntity> hasSectionTitle(String title) {
        return (root, query, cb) -> {
            if (title == null) return null;
            var sections = root.join("sections");
            return cb.like(cb.lower(sections.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<RegulationEntity> build(RegulationFilter filter) {
        return Specification
            .where(hasName(filter.getName()))
            .and(hasSectionTitle(filter.getHasSectionWithTitle()));
    }
}