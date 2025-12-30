package com.parejo.msvc_usuario.dtos.req;

import jakarta.validation.constraints.NotBlank;

// Para recibir las credenciales
public record LoginReqDTO(
        @NotBlank String email,
        @NotBlank String password
) {}

