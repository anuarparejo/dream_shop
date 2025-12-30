package com.parejo.msvc_usuario.controllers;

import com.parejo.msvc_usuario.dtos.req.UserReqDTO;
import com.parejo.msvc_usuario.dtos.req.UserSearchDTO;
import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import com.parejo.msvc_usuario.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResDTO> create(@Valid @RequestBody UserReqDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<UserResDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<UserResDTO>> filter(UserSearchDTO filters, Pageable pageable) {
        return ResponseEntity.ok(userService.findByFilters(filters, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResDTO> update(@PathVariable Long id, @Valid @RequestBody UserReqDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}