package main.java.com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<Product, Integer> items = new HashMap<>();

    // Menambahkan produk dengan validasi Quantity
    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity tidak valid: " + qty + " (Harus > 0)");
        }
        items.put(p, items.getOrDefault(p, 0) + qty);
        System.out.println("Berhasil menambah: " + p.getName() + " (Qty: " + qty + ")");
    }

    // Menghapus produk dengan validasi Keberadaan Produk
    public void removeProduct(Product p) throws ProductNotFoundException {
        if (!items.containsKey(p)) {
            throw new ProductNotFoundException("Gagal hapus: Produk " + p.getName() + " tidak ada di keranjang.");
        }
        items.remove(p);
        System.out.println("Berhasil menghapus: " + p.getName());
    }

    // Checkout dengan validasi Stok
    public void checkout() throws InsufficientStockException {
        System.out.println("\n--- Proses Checkout ---");
        // 1. Cek stok dulu
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int qtyNeeded = entry.getValue();
            
            if (product.getStock() < qtyNeeded) {
                throw new InsufficientStockException(
                    "Stok kurang untuk: " + product.getName() + 
                    " (Stok: " + product.getStock() + ", Diminta: " + qtyNeeded + ")"
                );
            }
        }

        // 2. Jika semua aman, baru kurangi stok
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
            System.out.println("Checkout sukses: " + entry.getKey().getName() + " - Stok sisa: " + entry.getKey().getStock());
        }
    }
}