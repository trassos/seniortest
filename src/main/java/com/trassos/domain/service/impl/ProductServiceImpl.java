package com.trassos.domain.service.impl;

import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Product;
import com.trassos.persistence.ProductJpaRepository;
import com.trassos.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository productJpaRepository;
    @Autowired
    public ProductServiceImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }


    @Override
    @Transactional
    public Product createProduct(Product product) {
        validateProduct(product, new AtomicInteger(0), "createProduct");
        return productJpaRepository.save(product);
    }

    @Override
    @Transactional
    public List<Product> createProducts(List<Product> products){
        AtomicInteger index = new AtomicInteger(0);
        products.forEach(product -> {
            index.getAndIncrement();
            validateProduct(product, index, "createProducts");
        });
        return productJpaRepository.saveAll(products);
    }

    @Override
    public Product changeProductStatus(UUID productId, boolean active) {
        Product product = productJpaRepository.findById(productId).
                orElseThrow(() -> new SeniorStandardException("Produto não encontrado"));
        if (product.isActive() == active) {
            throw new SeniorStandardException("O status do produto já está " + (active ? "ativo" : "inativo"));
        }
        product.setActive(active);
        return productJpaRepository.save(product);
    }

    @Override
    public Product getProductById(UUID productId) {
        return productJpaRepository.findById(productId).orElseThrow(() -> new SeniorStandardException("Produto não encontrado"));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public Product updateProduct(UUID productId, Product updatedProduct) {
        validateProduct(updatedProduct, new AtomicInteger(0), "updateProduct");
        Product product = productJpaRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return productJpaRepository.save(product);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteProduct(UUID productId) {
        Optional<Product> product = productJpaRepository.findById(productId);
        if (product.isPresent()) {
            productJpaRepository.delete(product.get());
            return true;
        } else {
            throw new SeniorStandardException("Produto não encontrado");
        }
    }

    private void validateProduct(Product product, AtomicInteger index, String method) {
        switch (method) {
            case "createProduct":
                if (product.getId() != null) {
                    throw new SeniorStandardException("O ID do produto deve ser nulo para criar um novo.");
                }
                if (product.getName() == null || product.getName().isEmpty()) {
                    throw new SeniorStandardException("O nome do produto é obrigatório.");
                }
                if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new SeniorStandardException("O preço do produto é obrigatório e deve ser maior que zero.");
                }
                break;
            case "createProducts":
                if (product.getId() != null) {
                    throw new SeniorStandardException("O ID do "+ index+"º produto deve ser nulo para criar um novo.");
                }
                if (product.getName() == null || product.getName().isEmpty()) {
                    throw new SeniorStandardException("O nome do "+ index+"º produto é obrigatório.");
                }
                if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new SeniorStandardException("O preço do "+ index+"º produto é obrigatório e deve ser maior que zero.");
                }
                break;
            case "updateProduct":
                if (product.getName() == null || product.getName().isEmpty()) {
                    throw new SeniorStandardException("O nome do produto é obrigatório.");
                }
                if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new SeniorStandardException("O preço do produto é obrigatório e deve ser maior que zero.");
                }
                break;
            default:
                throw new SeniorStandardException("Ocorreu um erro ao validar o produto.");
        }

    }

}
