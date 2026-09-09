package com.ecommerce.platform.productservice.service;

import com.ecommerce.platform.productservice.model.Product;
import com.ecommerce.platform.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "products", key = "#id")
    public Product getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public List<Product> getByCategory(String category) {
        return repository.findByCategory(category);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product create(Product product) {
        return repository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public Product update(String id, Product updated) {
        Product existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        existing.setStockQuantity(updated.getStockQuantity());
        return repository.save(existing);
    }

    @CacheEvict(value = "products", key = "#id")
    public void delete(String id) {
        repository.deleteById(id);
    }
}
