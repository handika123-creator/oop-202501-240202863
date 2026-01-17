package main.java.com.upb.agripos;

import main.java.com.upb.agripos.config.DatabaseConnection;
import main.java.com.upb.agripos.controller.ProductController;
import main.java.com.upb.agripos.model.Product;
import main.java.com.upb.agripos.view.ConsoleView;

public class AppMVC {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week10)");
        System.out.println("====================================================");

        // 1. Tes Singleton
        System.out.println("\n--- TEST SINGLETON ---");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        db1.connect();
        
        DatabaseConnection db2 = DatabaseConnection.getInstance(); // Tidak akan memanggil constructor lagi
        System.out.println("Apakah db1 sama dengan db2? " + (db1 == db2));

        // 2. Tes MVC
        System.out.println("\n--- TEST MVC PATTERN ---");
        Product model = new Product("P01", "Pupuk Organik");
        ConsoleView view = new ConsoleView();
        ProductController controller = new ProductController(model, view);

        controller.showProduct();
    }
}