package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.res.RoleResDTO;
import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.exceptions.ResourceNotFoundException;
import com.parejo.msvc_usuario.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleResDTO save(String rol) {
        final String finalRoleName = formatRoleName(rol);

        roleRepository.findByNameAndIsActiveTrue(finalRoleName)
                .ifPresent(r -> {
                    throw new RuntimeException("El rol " + finalRoleName + " ya existe");
                });

        Role role = Role.builder()
                .name(finalRoleName)
                .isActive(true)
                .build();

        Role saved = roleRepository.save(role);
        return new RoleResDTO(saved.getId(), saved.getName());
    }

    @Override
    @Transactional
    public RoleResDTO update(Long id, String newName) {
        final String finalRoleName = formatRoleName(newName);
        Role role = findRoleOrThrow(id);

        role.setName(finalRoleName);
        return new RoleResDTO(role.getId(), roleRepository.save(role).getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResDTO> findAll(Pageable pageable) {
        return roleRepository.findAllByIsActiveTrue(pageable)
                .map(r -> new RoleResDTO(r.getId(), r.getName()));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResDTO findById(Long id) {
        Role role = findRoleOrThrow(id);
        return new RoleResDTO(role.getId(), role.getName());

    }

    @Override
    @Transactional
    public void deleteLogically(Long id) {
        Role role = findRoleOrThrow(id);
        role.setIsActive(false);
        roleRepository.save(role);
    }

    private Role findRoleOrThrow(Long id) {
        return roleRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
    }

    private String formatRoleName(String rol) {
        String formattedRole = rol.trim().toUpperCase();

        if (!formattedRole.startsWith("ROLE_")) {
            if (formattedRole.startsWith("ROLE")) {
                formattedRole = formattedRole.replace("ROLE", "ROLE_");
            } else {
                formattedRole = "ROLE_" + formattedRole;
            }
        }
        return formattedRole;
    }
}