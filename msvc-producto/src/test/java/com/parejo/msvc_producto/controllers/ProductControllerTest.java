package com.parejo.msvc_producto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.req.ProductSearchDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.exceptions.ResourceNotFoundException;
import com.parejo.msvc_producto.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.parejo.msvc_producto.util.ProductData.*;
import static com.parejo.msvc_producto.util.ProductData.createProductResDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)

class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("POST /api/products - Debe crear un producto y retornar 201")
    void create_ShouldReturnCreated() throws Exception {

        ProductReqDTO reqDto = createProductReqDTO();
        ProductResDTO resDto = createProductResDTO();

        when(productService.save(any(ProductReqDTO.class))).thenReturn(resDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).save(reqDto);

    }

    @Test
    void filterProducts_ShouldReturnStatusOk() throws Exception {
        // GIVEN
        when(productService.findByFilters(any(ProductSearchDTO.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        // WHEN & THEN
        mockMvc.perform(get("/api/products/filter")
                        .param("name", "Laptop")
                        .param("minPrice", "500")
                        .param("onlyWithStock", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("PUT /{id} - Debe actualizar y retornar 200")
    void update_ShouldReturnOk() throws Exception {
        ProductReqDTO req = createProductReqDTO();
        ProductResDTO res = createProductResDTO();

        when(productService.update(eq(1L), any(ProductReqDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    @DisplayName("POST /api/products - Debe retornar BadRequest si la informacion no es valida")
    void create_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        ProductReqDTO invalidDto = new ProductReqDTO(
                "",
                "",
                new BigDecimal("-10.00"),
                100,
                1L,
                "httṕs://google.com",
                0.00);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());


        verify(productService, never()).save(any());
    }

    @Test
    @DisplayName("GET /api/products - Debe retornar una lista de productos")
    void list_ShouldReturnList() throws Exception {
        ProductResDTO productResDTO = createProductResDTO();
        Page<ProductResDTO> page = new PageImpl<>(List.of(productResDTO));

        when(productService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"));
        verify(productService).findAll(any());
    }

    @Test
    @DisplayName("GET /api/products - Debe retornar 500 cuando ocurre un error inesperado")
    void list_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {

        when(productService.findAll(any(Pageable.class)))
                .thenThrow(new RuntimeException("Error de conexión a la base de datos"));


        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()); // Verifica HTTP 500

        verify(productService).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/products/id/{id} - Debe retornar un producto por su id")
    void getById_ShouldReturnProduct() throws Exception {
        Long id = 1L;
        ProductResDTO productResDTO = createProductResDTO();

        when(productService.findById(id)).thenReturn(productResDTO);

        mockMvc.perform(get("/api/products/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productResDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Laptop"));
        verify(productService).findById(id);
    }

    @Test
    @DisplayName("GET /api/products/id/{id} - Debe retornar notFound cuando el producto no exista")
    void getById_ShouldReturnNotFound_WhenProductNotFound() throws Exception {
        Long idNotFound = 1L;

        when(productService.findById(idNotFound))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        mockMvc.perform(get("/api/products/id/{id}", idNotFound)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(idNotFound);
    }

    @Test
    @DisplayName("PUT /api/products/disable/{id} - Debe retornar 204 si el producto es desactivado")
    void delete_ShouldDeleteProduct() throws Exception {
        Long id = 1L;

        mockMvc.perform(put("/api/products/disable/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("PUT /api/products/disable/{id} - Debe retornar 404 si el id no esta activo")
    void disable_ShouldReturnNotFound_WhenIdIsInvalid() throws Exception {
        Long idNotActive = 1L;

        doThrow(new ResourceNotFoundException("No encontrado"))
                .when(productService).deleteById(idNotActive);


        mockMvc.perform(put("/api/products/disable/{id}", idNotActive)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService).deleteById(idNotActive);
    }












}
