package com.upb.agripos.service;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private List<CartItem> items = new ArrayList<>();

    public void addToCart(Product product, int quantity) {
        for (CartItem existing : items) {
            if (existing.getProduct().getCode().equals(product.getCode())) {
                return; // Logic sederhana: kalau ada, abaikan (atau bisa tambah qty)
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public List<CartItem> getCartItems() { return items; }

    public double calculateTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public void clearCart() { items.clear(); }
}