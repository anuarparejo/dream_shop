package com.parejo.msvc_usuarios.dtos.req;

import java.util.Set;

public record UserReqDTO(
        String name,
        String email,
        String password,
        Set<Long> rolesIds
) {}