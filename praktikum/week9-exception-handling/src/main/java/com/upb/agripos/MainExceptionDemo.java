package main.java.com.upb.agripos;

public class MainExceptionDemo {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week9)");
        System.out.println("====================================================");

        ShoppingCart cart = new ShoppingCart();
        // Stok awal hanya 3
        Product p1 = new Product("P01", "Pupuk Organik", 25000, 3); 

        // SKENARIO 1: Menambah Quantity Negatif (Error)
        System.out.println("\n[Test 1] Input Quantity Negatif");
        try {
            cart.addProduct(p1, -5);
        } catch (InvalidQuantityException e) {
            System.err.println("TERTANGKAP: " + e.getMessage());
        }

        // SKENARIO 2: Menghapus Produk yang belum ada (Error)
        System.out.println("\n[Test 2] Hapus Produk Kosong");
        try {
            cart.removeProduct(p1);
        } catch (ProductNotFoundException e) {
            System.err.println("TERTANGKAP: " + e.getMessage());
        }

        // SKENARIO 3: Checkout melebihi stok (Error)
        System.out.println("\n[Test 3] Checkout Melebihi Stok");
        try {
            cart.addProduct(p1, 10); // Minta 10, padahal stok cuma 3
            cart.checkout();
        } catch (Exception e) {
            // Menangkap InvalidQuantity atau InsufficientStock
            System.err.println("TERTANGKAP: " + e.getMessage());
        }

        // SKENARIO 4: Normal (Sukses)
        System.out.println("\n[Test 4] Transaksi Normal");
        try {
            // Reset keranjang (manual logic untuk simulasi)
            cart = new ShoppingCart(); 
            cart.addProduct(p1, 2); // Beli 2, Stok 3 (Cukup)
            cart.checkout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}