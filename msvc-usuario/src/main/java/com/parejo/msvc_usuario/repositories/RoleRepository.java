package com.parejo.msvc_usuario.repositories;

import com.parejo.msvc_usuario.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Collection;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameAndIsActiveTrue(String name);
    Optional<Role> findByIdAndIsActiveTrue(Long id);
    Page<Role> findAllByIsActiveTrue();
    Set<Role> findAllByIdInAndIsActiveTrue(Collection<Long> ids);
}