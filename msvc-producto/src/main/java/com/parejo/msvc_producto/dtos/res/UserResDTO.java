package com.parejo.msvc_producto.dtos.res;

import java.util.Set;

public record UserResDTO(
        Long id,
        String name,
        String email,
        Set<RoleResDTO> roles
) {}