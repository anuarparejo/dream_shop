package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret:clave_secreta_muy_larga_para_mi_proyecto_dreamshop_2025}")
    private String secretKey;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // Agregamos los roles al token para que otros microservicios los vean
        claims.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        claims.put("name", user.getName());

        // 24 horas en milisegundos
        long EXPIRATION_TIME = 86_400_000;
        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}