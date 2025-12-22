package com.parejo.msvc_producto.dtos.res;


import java.time.LocalDateTime;

public record CategoryResDTO(
        Long
        id,

        String
        name,

        String
        type,

        LocalDateTime
        createdAt,

        LocalDateTime
        updatedAt

) {
}
