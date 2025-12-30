package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.req.UserReqDTO;
import com.parejo.msvc_usuario.dtos.req.UserSearchDTO;
import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.entities.User;
import com.parejo.msvc_usuario.exceptions.ResourceNotFoundException;
import com.parejo.msvc_usuario.mappers.UserMapper;
import com.parejo.msvc_usuario.repositories.RoleRepository;
import com.parejo.msvc_usuario.repositories.UserRepository;
import com.parejo.msvc_usuario.specifications.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResDTO save(UserReqDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        if (dto.rolesIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllByIdInAndIsActiveTrue(dto.rolesIds()));
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResDTO(savedUser);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResDTO> findByFilters(UserSearchDTO filters, Pageable pageable) {
        Specification<User> spec = Specification.where(UserSpecifications.isNotDeleted())
                .and(UserSpecifications.hasName(filters.name()))
                .and(UserSpecifications.hasEmail(filters.email()))
                .and(UserSpecifications.hasRoleId(filters.roleId()));

        return userRepository.findAll(spec, pageable)
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
        User user =  findUserOrThrow(id);

        user.setName(dto.name());
        user.setEmail(dto.email());
        if(dto.password()!=null) user.setPassword(passwordEncoder.encode(dto.password()));

        if (dto.rolesIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllByIdInAndIsActiveTrue(dto.rolesIds()));
            user.setRoles(roles);
        }

        return userMapper.toResDTO(userRepository.save(user));
    }


    @Override
    @Transactional(readOnly = true)
    public UserResDTO findById(Long id) {
        User user = findUserOrThrow(id);
        return userMapper.toResDTO(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findUserOrThrow(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}