package com.parejo.msvc_producto.services;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResDTO> findAll(Pageable pageable);
    ProductResDTO save(ProductReqDTO dto);
    ProductResDTO findById(Long id);
    Page<ProductResDTO> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
    void deleteById(Long id);
    ProductResDTO update(Long id, ProductReqDTO dto);
}
