package com.parejo.msvc_producto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parejo.msvc_producto.dtos.req.CategoryReqDTO;
import com.parejo.msvc_producto.dtos.res.CategoryResDTO;
import com.parejo.msvc_producto.exceptions.ResourceNotFoundException;
import com.parejo.msvc_producto.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.parejo.msvc_producto.util.ProductData.createCategoryReqDTO;
import static com.parejo.msvc_producto.util.ProductData.createCategoryResDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/categories - Debe retornar página de categorías")
    void getAll_ShouldReturnPage() throws Exception {
        Page<CategoryResDTO> page = new PageImpl<>(List.of(createCategoryResDTO()));
        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Tecnologia"));
    }

    @Test
    @DisplayName("POST /api/categories - Debe crear categoría y retornar 201")
    void create_ShouldReturnCreated() throws Exception {
        CategoryReqDTO req = createCategoryReqDTO();
        CategoryResDTO res = createCategoryResDTO();
        when(categoryService.save(any(CategoryReqDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/categories - Debe retornar 400 si el nombre es inválido")
    void create_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        CategoryReqDTO invalidReq = new CategoryReqDTO("", ""); // Nombre vacío

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /disable/{id} - Debe retornar 204")
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(put("/api/categories/disable/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(1L);
    }

    @Test
    @DisplayName("PUT /disable/{id} - Debe retornar 409 si tiene productos enlazados")
    void delete_ShouldReturnConflict_WhenHasProducts() throws Exception {

        doThrow(new DataIntegrityViolationException("Conflict"))
                .when(categoryService).deleteById(1L);

        mockMvc.perform(put("/api/categories/disable/{id}", 1L))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /{id} - Debe actualizar y retornar 200")
    void update_ShouldReturnOk() throws Exception {
        CategoryReqDTO req = createCategoryReqDTO();
        CategoryResDTO res = createCategoryResDTO();
        when(categoryService.update(eq(1L), any(CategoryReqDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tecnologia"));
    }

    @Test
    @DisplayName("GET /{id} - Debe retornar 404 si no existe")
    void getById_ShouldReturnNotFound() throws Exception {
        when(categoryService.findById(1L))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/categories/{id}", 1L))
                .andExpect(status().isNotFound());
    }


}