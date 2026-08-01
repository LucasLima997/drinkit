package com.drinkit.controller;

import com.drinkit.model.CartViewItem;
import com.drinkit.model.CustomerOrder;
import com.drinkit.model.OrderItem;
import com.drinkit.model.Product;
import com.drinkit.repository.CustomerOrderRepository;
import com.drinkit.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CheckoutController {

    private final ProductRepository productRepository;
    private final CustomerOrderRepository customerOrderRepository;

    public CheckoutController(ProductRepository productRepository, CustomerOrderRepository customerOrderRepository) {
        this.productRepository = productRepository;
        this.customerOrderRepository = customerOrderRepository;
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        List<CartViewItem> cartItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey()).orElse(null);

            if (product != null) {
                CartViewItem item = new CartViewItem(product, entry.getValue());
                cartItems.add(item);
                total = total.add(item.getSubtotal());
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String confirmCheckout(
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            @RequestParam String deliveryAddress,
            HttpSession session
    ) {
        Map<Long, Integer> cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        BigDecimal total = BigDecimal.ZERO;
        List<CartViewItem> cartItems = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey()).orElse(null);

            if (product != null) {
                CartViewItem item = new CartViewItem(product, entry.getValue());
                cartItems.add(item);
                total = total.add(item.getSubtotal());
            }
        }

        CustomerOrder order = new CustomerOrder(customerName, customerEmail, deliveryAddress, total);

        for (CartViewItem item : cartItems) {
           OrderItem orderItem = new OrderItem(
        item.getProduct().getId(),
        item.getProduct().getName(),
        item.getQuantity(),
        item.getProduct().getPrice(),
        item.getSubtotal()
);

            order.addItem(orderItem);
        }

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        session.removeAttribute("cart");

        return "redirect:/order-confirmation/" + savedOrder.getId();
    }

    @GetMapping("/order-confirmation/{id}")
    public String orderConfirmation(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + id));

        model.addAttribute("order", order);

        return "order-confirmation";
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Object cartObject = session.getAttribute("cart");

        if (cartObject == null) {
            return Map.of();
        }

        return (Map<Long, Integer>) cartObject;
    }
}