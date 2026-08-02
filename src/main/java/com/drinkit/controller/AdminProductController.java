package com.drinkit.controller;

import com.drinkit.model.Product;
import com.drinkit.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class AdminProductController {

    private final ProductRepository productRepository;

    public AdminProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/admin/products")
    public String adminProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin-products";
    }

    @GetMapping("/admin/products/new")
    public String newProductForm() {
        return "admin-product-form";
    }

    @PostMapping("/admin/products")
    public String createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam String category,
            @RequestParam String imageUrl,
            @RequestParam(defaultValue = "false") boolean alcoholic
    ) {
        Product product = new Product(
                name,
                description,
                price,
                category,
                imageUrl,
                alcoholic
        );

        productRepository.save(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}