package com.parejo.msvc_producto.controllers;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.req.ProductSearchDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResDTO>> list(
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResDTO>> filterProducts(
            ProductSearchDTO filters,
            Pageable pageable) {

        return ResponseEntity.ok(productService.findByFilters(filters, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductResDTO> create(@Valid @RequestBody ProductReqDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResDTO> update(@PathVariable Long id, @Valid @RequestBody ProductReqDTO dto) {
        return ResponseEntity.ok(productService.update(id,dto));
    }


}