package com.james.demo;

import com.james.demo.persistence.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) {
        item.setId(UUID.randomUUID());
        return item;
    }
}
