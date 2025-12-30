package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.req.LoginReqDTO;
import com.parejo.msvc_usuario.dtos.res.LoginResDTO;
import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.entities.User;
import com.parejo.msvc_usuario.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Crearemos esto a continuación

    public LoginResDTO login(LoginReqDTO dto) {

        User user = userRepository.findByEmailAndIsActiveTrue(dto.email())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user);

        return new LoginResDTO(
                token,
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}