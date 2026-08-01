package com.drinkit.model;

import java.math.BigDecimal;

public class CartViewItem {

    private Product product;
    private int quantity;

    public CartViewItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}