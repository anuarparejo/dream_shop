package com.parejo.msvc_usuario;

import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder()
                    .name("ROLE_USER")
                    .isActive(true)
                    .build());
            roleRepository.save(Role.builder()
                    .name("ROLE_ADMIN")
                    .isActive(true)
                    .build());
        }
    }
}