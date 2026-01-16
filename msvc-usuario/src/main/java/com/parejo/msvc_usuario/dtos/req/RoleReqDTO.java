package com.parejo.msvc_usuario.dtos.req;

import jakarta.validation.constraints.NotBlank;

public record RoleReqDTO(@NotBlank String name) {}