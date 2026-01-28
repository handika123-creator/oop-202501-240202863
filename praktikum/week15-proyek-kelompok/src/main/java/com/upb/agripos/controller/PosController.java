package com.upb.agripos.controller;

import com.upb.agripos.model.*;
import com.upb.agripos.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PosController {
    private final ProductService productService = ProductService.getInstance();
    private final CartService cartService = new CartService();

    // --- Produk (Database) ---
    public ObservableList<Product> loadProducts() {
        try {
            return FXCollections.observableArrayList(productService.getAllProducts());
        } catch (Exception e) {
            return FXCollections.observableArrayList();
        }
    }

    public void addProductToDb(String c, String n, double p, int s) throws Exception {
        productService.addProduct(new Product(c, n, p, s));
    }

    public void deleteProduct(String code) throws Exception {
        productService.deleteProduct(code);
    }

    // --- Keranjang (Memory) ---
    public void addToCart(Product p, int qty) {
        cartService.addToCart(p, qty);
    }

    public ObservableList<CartItem> getCartItems() {
        return FXCollections.observableArrayList(cartService.getCartItems());
    }

    public double getCartTotal() {
        return cartService.calculateTotal();
    }

    public void checkout() {
        cartService.clearCart();
    }
}