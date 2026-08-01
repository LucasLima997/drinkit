package com.drinkit.config;

import com.drinkit.model.Product;
import com.drinkit.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {

                Product mojito = new Product(
                        "Mojito Starter Kit",
                        "A complete kit to prepare a classic Mojito at home. Includes mint, lime, sugar syrup, rum suggestion, garnish and a digital guide.",
                        new BigDecimal("29.99"),
                        "Cocktail Kit",
                        "/images/mojito.jpg",
                        true
                );

                Product margarita = new Product(
                        "Margarita Kit",
                        "A cocktail kit designed to help customers prepare a simple Margarita at home with clear step-by-step guidance.",
                        new BigDecimal("32.99"),
                        "Cocktail Kit",
                        "/images/margarita.jpg",
                        true
                );

                Product virginMojito = new Product(
                        "Virgin Mojito Mocktail Kit",
                        "A non-alcoholic mocktail kit for customers who want to enjoy a fresh Mojito-style drink without alcohol.",
                        new BigDecimal("24.99"),
                        "Mocktail Kit",
                        "/images/virgin-mojito.jpg",
                        false
                );

                productRepository.save(mojito);
                productRepository.save(margarita);
                productRepository.save(virginMojito);
            }
        };
    }
}