package com.parejo.msvc_usuario.dtos.req;

import jakarta.validation.constraints.*;
import java.util.Set;

public record UserReqDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String name,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El formato del email no es válido")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotEmpty(message = "El usuario debe tener al menos un rol asignado")
        Set<Long> rolesIds
) {}