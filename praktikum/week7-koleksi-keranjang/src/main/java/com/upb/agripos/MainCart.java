package main.java.com.upb.agripos;

public class MainCart {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week7)");
        System.out.println("====================================================");

        // Data Produk Dummy
        Product p1 = new Product("BNH-01", "Benih Padi", 50000);
        Product p2 = new Product("PPK-02", "Pupuk Urea", 30000);
        Product p3 = new Product("ALT-03", "Cangkul", 75000);

        // --- Skenario 1: Menggunakan ArrayList ---
        System.out.println("\nTESTING ARRAYLIST (Basic)");
        ShoppingCart listCart = new ShoppingCart();
        
        listCart.addProduct(p1); // Beli Benih
        listCart.addProduct(p2); // Beli Pupuk
        listCart.addProduct(p1); // Beli Benih lagi (List akan mencatatnya 2 baris terpisah)
        
        listCart.printCart();

        listCart.removeProduct(p2); // Hapus Pupuk
        listCart.printCart();

        // --- Skenario 2: Menggunakan HashMap (Advanced) ---
        System.out.println("\nTESTING HASHMAP (Quantity Logic)");
        ShoppingCartMap mapCart = new ShoppingCartMap();
        
        mapCart.addProduct(p1); // Qty jadi 1
        mapCart.addProduct(p1); // Qty jadi 2
        mapCart.addProduct(p3); // Qty jadi 1
        
        mapCart.printCart(); // Harusnya Benih x2
        
        mapCart.removeProduct(p1); // Qty Benih turun jadi 1
        mapCart.printCart();
    }
}