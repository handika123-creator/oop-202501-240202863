package com.upb.agripos.service;

import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import java.util.List;

public class CartService {
    private Cart cart = new Cart();

    public void addToCart(Product product, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Jumlah harus lebih dari 0!");
        }
        if (product.getStock() < quantity) {
            throw new Exception("Stok tidak cukup!");
        }
        cart.addItem(product, quantity);
    }

    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    public double calculateTotal() {
        return cart.getTotalPrice();
    }

    public void clearCart() {
        cart.clear();
    }
}