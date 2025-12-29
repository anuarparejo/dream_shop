package com.parejo.msvc_usuarios.dtos.res;

import java.util.Set;

public record UserResDTO(
        Long id,
        String name,
        String email,
        Boolean isActive,
        Set<RoleResDTO> roles
) {}