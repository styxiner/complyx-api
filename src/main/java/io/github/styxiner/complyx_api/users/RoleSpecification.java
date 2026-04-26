package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RoleSpecification implements Specification<UserEntity>{
    private static final long serialVersionUID = 1L;
	private final UUID ROLEID;

    public RoleSpecification(UUID roleId) {
        this.ROLEID = roleId;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (ROLEID == null) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.equal(root.get("role").get("id"), ROLEID);
    }
}
