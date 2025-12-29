package com.parejo.msvc_usuarios.mappers;

import com.parejo.msvc_usuarios.dtos.req.UserReqDTO;
import com.parejo.msvc_usuarios.dtos.res.RoleResDTO;
import com.parejo.msvc_usuarios.dtos.res.UserResDTO;
import com.parejo.msvc_usuarios.entities.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserReqDTO dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .isActive(true)
                .roles(new HashSet<>())
                .build();
    }

    public UserResDTO toResDTO(User entity) {
        Set<RoleResDTO> roleDtos = null;

        if (entity.getRoles() != null) {
            roleDtos = entity.getRoles().stream()
                    .map(role -> new RoleResDTO(role.getId(), role.getName()))
                    .collect(Collectors.toSet());
        }

        return new UserResDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getIsActive(),
                roleDtos
        );
    }
}