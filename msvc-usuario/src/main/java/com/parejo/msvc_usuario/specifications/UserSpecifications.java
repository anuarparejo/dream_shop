package com.parejo.msvc_usuario.specifications;

import com.parejo.msvc_usuario.entities.User;
import com.parejo.msvc_usuario.entities.Role;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), true);
    }

    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> email == null ? null :
                cb.equal(root.get("email"), email);
    }

    public static Specification<User> hasRoleId(Long roleId) {
        return (root, query, cb) -> {
            if (roleId == null) return null;
            // Join con la tabla de roles para filtrar por ID
            Join<User, Role> rolesJoin = root.join("roles");
            return cb.equal(rolesJoin.get("id"), roleId);
        };
    }
}