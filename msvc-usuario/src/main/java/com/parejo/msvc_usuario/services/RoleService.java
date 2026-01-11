package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.res.RoleResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleResDTO save(String name);
    RoleResDTO update(Long id, String newName);
    Page<RoleResDTO> findAll(Pageable pageable);
    RoleResDTO findById(Long id);
    void deleteLogically(Long id);
}