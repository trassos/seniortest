package com.trassos.domain.service;

import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Product;
import com.trassos.domain.service.impl.ProductServiceImpl;
import com.trassos.persistence.ProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void updateProductWithInactiveProductTest() {
        UUID productId = UUID.randomUUID();
        Product updatedProduct = new Product();
        updatedProduct.setActive(false);
        updatedProduct.setName("Balde de Café");
        updatedProduct.setPrice(BigDecimal.TEN);
        Product product = new Product();
        when(productJpaRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        when(productJpaRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.updateProduct(productId, updatedProduct));
        verify(productJpaRepository, times(1)).findById(productId);
        verify(productJpaRepository, times(1)).save(product);
    }

    @Test
    public void deleteNonexistentProductTest() {
        UUID productId = UUID.randomUUID();
        when(productJpaRepository.findById(productId)).thenReturn(java.util.Optional.empty());
        assertThrows(SeniorStandardException.class, () -> productService.deleteProduct(productId));
        verify(productJpaRepository, times(1)).findById(productId);
    }

    @Test
    public void deleteExistentProductTest() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        when(productJpaRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        assertTrue(productService.deleteProduct(productId));
        verify(productJpaRepository, times(1)).findById(productId);
        verify(productJpaRepository, times(1)).delete(product);
    }

    @Test
    public void createProductWithNullNameTest() {
        Product product = new Product();
        product.setName(null);
        assertThrows(SeniorStandardException.class, () -> productService.createProduct(product));
    }

    @Test
    public void createProductWithNullPriceTest() {
        Product product = new Product();
        product.setPrice(null);
        assertThrows(SeniorStandardException.class, () -> productService.createProduct(product));
    }

    @Test
    public void updateProductWithNullNameTest() {
        UUID productId = UUID.randomUUID();
        Product updatedProduct = new Product();
        updatedProduct.setName(null);
        assertThrows(SeniorStandardException.class, () -> productService.updateProduct(productId, updatedProduct));
    }

    @Test
    public void updateProductWithNullPriceTest() {
        UUID productId = UUID.randomUUID();
        Product updatedProduct = new Product();
        updatedProduct.setPrice(null);
        assertThrows(SeniorStandardException.class, () -> productService.updateProduct(productId, updatedProduct));
    }

    @Test
    public void updateProductWithPriceLessOrEqualToZeroTest() {
        UUID productId = UUID.randomUUID();
        Product updatedProduct = new Product();
        updatedProduct.setPrice(BigDecimal.ZERO);
        assertThrows(SeniorStandardException.class, () -> productService.updateProduct(productId, updatedProduct));
    }

    @Test
    public void createProductWithPriceLessOrEqualToZeroTest() {
        Product product = new Product();
        product.setPrice(BigDecimal.ZERO);
        assertThrows(SeniorStandardException.class, () -> productService.createProduct(product));
    }

    @Test
    public void createProductWithNotNullIdTest() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        assertThrows(SeniorStandardException.class, () -> productService.createProduct(product));
    }

    @Test
    public void updateProductWithNullIdTest() {
        Product updatedProduct = new Product();
        updatedProduct.setId(null);
        assertThrows(SeniorStandardException.class, () -> productService.updateProduct(UUID.randomUUID(), updatedProduct));
    }

    @Test
    public void createProductWithNullIdTest() {
        Product product = new Product();
        product.setId(null);
        product.setName("Balde de Café");
        product.setPrice(BigDecimal.TEN);
        when(productJpaRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.createProduct(product));
        verify(productJpaRepository, times(1)).save(product);
    }

}
