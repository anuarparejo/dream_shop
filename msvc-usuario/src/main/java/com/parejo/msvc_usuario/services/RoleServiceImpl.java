package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.res.RoleResDTO;
import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleResDTO save(String name) {
        roleRepository.findByNameAndIsActiveTrue(name)
                .ifPresent(r -> { throw new RuntimeException("El rol ya existe y está activo"); });

        Role role = Role.builder()
                .name(name.toUpperCase())
                .isActive(true)
                .build();

        Role saved = roleRepository.save(role);
        return new RoleResDTO(saved.getId(), saved.getName());
    }

    @Override
    @Transactional
    public RoleResDTO update(Long id, String newName) {
        Role role = roleRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado o inactivo"));

        role.setName(newName.toUpperCase());
        return new RoleResDTO(role.getId(), roleRepository.save(role).getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResDTO> findAll() {
        return roleRepository.findAllByIsActiveTrue()
                .map(r -> new RoleResDTO(r.getId(), r.getName()));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResDTO findById(Long id) {
        return roleRepository.findByIdAndIsActiveTrue(id)
                .map(r -> new RoleResDTO(r.getId(), r.getName()))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void deleteLogically(Long id) {
        Role role = roleRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: Rol no encontrado"));
        role.setIsActive(false);
        roleRepository.save(role);
    }
}