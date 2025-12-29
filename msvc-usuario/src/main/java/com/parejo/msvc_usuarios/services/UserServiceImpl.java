package com.parejo.msvc_usuarios.services;

import com.parejo.msvc_usuarios.dtos.req.UserReqDTO;
import com.parejo.msvc_usuarios.dtos.res.UserResDTO;
import com.parejo.msvc_usuarios.entities.Role;
import com.parejo.msvc_usuarios.entities.User;
import com.parejo.msvc_usuarios.mappers.UserMapper;
import com.parejo.msvc_usuarios.repositories.RoleRepository;
import com.parejo.msvc_usuarios.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.rolesIds()));
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResDTO findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.deleteById(id);
        userRepository.save(user);
    }
}