package com.parejo.msvc_usuario.dtos.res;

import java.util.Set;

// Para devolver el token y datos básicos
public record LoginResDTO(
        String token,
        String email,
        Set<String> roles
) {}