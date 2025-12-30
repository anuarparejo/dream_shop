package com.parejo.msvc_usuario.services;

import com.parejo.msvc_usuario.dtos.req.UserReqDTO;
import com.parejo.msvc_usuario.dtos.req.UserSearchDTO;
import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
    UserResDTO save(UserReqDTO dto);

    @Transactional(readOnly = true)
    Page<UserResDTO> findByFilters(UserSearchDTO filters, Pageable pageable);

    Page<UserResDTO> findAll(Pageable pageable);

    UserResDTO update(Long id, UserReqDTO dto);

    UserResDTO findById(Long id);

    void delete(Long id);
}