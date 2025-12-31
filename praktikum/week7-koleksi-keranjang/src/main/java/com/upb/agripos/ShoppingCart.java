package main.java.com.upb.agripos;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p) {
        items.add(p);
        System.out.println("Berhasil menambahkan: " + p.getName());
    }

    public void removeProduct(Product p) {
        if(items.remove(p)) {
            System.out.println("Berhasil menghapus: " + p.getName());
        } else {
            System.out.println("Barang tidak ditemukan.");
        }
    }

    public double getTotal() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void printCart() {
        System.out.println("\n--- Struk Belanja (List) ---");
        if (items.isEmpty()) {
            System.out.println("Keranjang kosong.");
        } else {
            for (Product p : items) {
                System.out.println("- " + p.getCode() + " " + p.getName() + " : Rp" + p.getPrice());
            }
            System.out.println("Total: Rp" + getTotal());
        }
        System.out.println("---------------------------");
    }
}