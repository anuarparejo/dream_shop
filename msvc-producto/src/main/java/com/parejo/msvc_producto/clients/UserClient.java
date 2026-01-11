package com.parejo.msvc_producto.clients;

import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con el nombre del msvc-usuario en el Gateway o Eureka
@FeignClient(name = "msvc-usuario", url = "http://localhost:8082/api/users")
public interface UserClient {

    @GetMapping("/{id}")
    UserResDTO getUserById(@PathVariable("id") Long id);
}