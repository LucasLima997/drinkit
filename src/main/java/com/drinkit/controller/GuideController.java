package com.drinkit.controller;

import com.drinkit.model.CustomerOrder;
import com.drinkit.model.Product;
import com.drinkit.repository.CustomerOrderRepository;
import com.drinkit.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GuideController {

    private final CustomerOrderRepository customerOrderRepository;
    private final ProductRepository productRepository;

    public GuideController(CustomerOrderRepository customerOrderRepository, ProductRepository productRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/orders/{id}/guides")
    public String orderGuides(@PathVariable Long id, Model model) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + id));

        model.addAttribute("order", order);

        return "my-guides";
    }

    @GetMapping("/guides/product/{id}")
    public String guideDetails(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));

        model.addAttribute("product", product);

        return "guide-detail";
    }
}