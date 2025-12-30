package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.req.UserReqDTO;
import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.entities.User;
import com.parejo.msvc_usuario.mappers.UserMapper;
import com.parejo.msvc_usuario.repositories.RoleRepository;
import com.parejo.msvc_usuario.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResDTO save(UserReqDTO dto) {
        User user = userMapper.toEntity(dto);
        if (dto.rolesIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllByIdInAndIsActiveTrue(dto.rolesIds()));
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResDTO(savedUser);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResDTO> findByName(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable)
                .map(userMapper::toResDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResDTO> findAll(Pageable pageable) {
        return userRepository.findAllByIsActiveTrue(pageable)
                .map(userMapper::toResDTO);

    }

    @Override
    @Transactional
    public UserResDTO update(Long id, UserReqDTO dto) {
        User user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        if (dto.rolesIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllByIdInAndIsActiveTrue(dto.rolesIds()));
            user.setRoles(roles);
        }

        return userMapper.toResDTO(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResDTO findByEmail(String email) {
        return userRepository.findByEmailAndIsActiveTrue(email)
                .map(userMapper::toResDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResDTO findById(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                .map(userMapper::toResDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIsActive(false);
        userRepository.save(user);
    }
}