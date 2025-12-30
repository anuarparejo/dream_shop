package com.parejo.msvc_usuario.dtos.req;

public record UserSearchDTO(
        String name,
        String email,
        Long roleId
) {}