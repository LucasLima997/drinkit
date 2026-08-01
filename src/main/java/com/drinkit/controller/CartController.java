package com.drinkit.controller;

import com.drinkit.model.CartViewItem;
import com.drinkit.model.Product;
import com.drinkit.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCart(session);

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

        return "cart";
    }

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);

        int currentQuantity = cart.getOrDefault(id, 0);
        cart.put(id, currentQuantity + 1);

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        cart.remove(id);
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Object cartObject = session.getAttribute("cart");

        if (cartObject == null) {
            Map<Long, Integer> cart = new HashMap<>();
            session.setAttribute("cart", cart);
            return cart;
        }

        return (Map<Long, Integer>) cartObject;
    }
}