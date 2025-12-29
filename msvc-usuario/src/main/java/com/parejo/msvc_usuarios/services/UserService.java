package com.parejo.msvc_usuarios.services;

import com.parejo.msvc_usuarios.dtos.req.UserReqDTO;
import com.parejo.msvc_usuarios.dtos.res.UserResDTO;
import java.util.List;

public interface UserService {
    UserResDTO save(UserReqDTO dto);
    List<UserResDTO> findAll();
    UserResDTO findById(Long id);
    void delete(Long id);
}