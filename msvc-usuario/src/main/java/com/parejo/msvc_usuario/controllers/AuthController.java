package com.parejo.msvc_usuario.controllers;

import com.parejo.msvc_usuario.dtos.req.LoginReqDTO;
import com.parejo.msvc_usuario.dtos.res.LoginResDTO;
import com.parejo.msvc_usuario.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@Valid @RequestBody LoginReqDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}