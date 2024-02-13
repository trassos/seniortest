package com.trassos.domain.service;

import com.trassos.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(UUID productId);

    Page<Product> getAllProducts(Pageable pageable);

    Product updateProduct(UUID productId, Product updatedProduct);

    boolean deleteProduct(UUID productId);

    List<Product> createProducts(List<Product> products);

    Product changeProductStatus(UUID productId, boolean active);
}
