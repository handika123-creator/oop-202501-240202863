package main.java.com.upb.agripos.config;

public class DatabaseConnection {
    // 1. Static instance variable
    private static DatabaseConnection instance;

    // 2. Private constructor (agar tidak bisa di-new dari luar)
    private DatabaseConnection() {
        System.out.println("Database Connection Created (Hanya muncul sekali)");
    }

    // 3. Static method untuk akses global
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void connect() {
        System.out.println("Terhubung ke Database Agri-POS.");
    }
}