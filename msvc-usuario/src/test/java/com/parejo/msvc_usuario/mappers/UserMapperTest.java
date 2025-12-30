package com.parejo.msvc_usuario.mappers;

import com.parejo.msvc_usuario.dtos.req.UserReqDTO;
import com.parejo.msvc_usuario.dtos.res.UserResDTO;
import com.parejo.msvc_usuario.entities.Role;
import com.parejo.msvc_usuario.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        UserReqDTO dto = new UserReqDTO("Anuar", "test@test.com", "123", Set.of(1L));
        User result = userMapper.toEntity(dto);

        assertEquals(dto.name(), result.getName());
        assertTrue(result.getIsActive());
        assertNotNull(result.getRoles());
    }

    @Test
    void toResDTO_WithRoles_ShouldMapEverything() {
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").build();
        User user = User.builder()
                .id(1L).name("Anuar").email("a@a.com").isActive(true)
                .roles(Set.of(role))
                .build();

        UserResDTO result = userMapper.toResDTO(user);

        assertNotNull(result.roles());
        assertEquals(1, result.roles().size());
        assertEquals("ROLE_ADMIN", result.roles().iterator().next().name());
    }

    @Test
    void toResDTO_WithNullRoles_ShouldReturnNullRolesInDto() {
        User user = User.builder().id(1L).roles(null).build();
        UserResDTO result = userMapper.toResDTO(user);
        assertNull(result.roles());
    }
}