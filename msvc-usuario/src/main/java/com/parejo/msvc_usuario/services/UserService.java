package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.req.UserReqDTO;
import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    UserResDTO save(UserReqDTO dto);
    Page<UserResDTO> findByName(String name, Pageable pageable);
    Page<UserResDTO> findAll(Pageable pageable);
    UserResDTO update(Long id, UserReqDTO dto);
    UserResDTO findByEmail(String email);
    UserResDTO findById(Long id);
    void delete(Long id);
}