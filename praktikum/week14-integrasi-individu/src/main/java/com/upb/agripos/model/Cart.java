package com.upb.agripos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    // Menggunakan Map<String, CartItem> agar mudah cari berdasarkan Kode Produk
    private Map<String, CartItem> items = new HashMap<>();

    public void addItem(Product product, int qty) {
        if (items.containsKey(product.getCode())) {
            // Jika sudah ada, update jumlahnya
            items.get(product.getCode()).addQuantity(qty);
        } else {
            // Jika belum ada, tambah baru
            items.put(product.getCode(), new CartItem(product, qty));
        }
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public double getTotalPrice() {
        return items.values().stream()
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
    }
}