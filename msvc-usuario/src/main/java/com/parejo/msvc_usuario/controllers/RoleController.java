package com.parejo.msvc_usuario.controllers;

import com.parejo.msvc_usuario.dtos.res.RoleResDTO;
import com.parejo.msvc_usuario.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResDTO> create(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.save(body.get("name")));
    }

    @GetMapping
    public ResponseEntity<Page<RoleResDTO>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResDTO> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(roleService.update(id, body.get("name")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.deleteLogically(id);
        return ResponseEntity.noContent().build();
    }
}