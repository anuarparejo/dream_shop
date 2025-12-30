package com.parejo.msvc_usuario.repositories;

import com.parejo.msvc_usuario.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByIdAndIsActiveTrue(Long id);
    Page<User> findAllByIsActiveTrue(Pageable pageable);

    Optional<User> findByEmailAndIsActiveTrue(@NotBlank String email);
}