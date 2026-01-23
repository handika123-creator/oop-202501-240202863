package com.upb.agripos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;

public class MainDAOTest {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week11 - PostgreSQL)");
        
        // --- KONFIGURASI POSTGRESQL ---
        String url = "jdbc:postgresql://localhost:5432/agripos";
        String user = "postgres";     // Default user PostgreSQL
        String password = "1234"; // GANTI DENGAN PASSWORD ANDA SAAT INSTAL POSTGRES
        // ------------------------------

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            ProductDAO dao = new ProductDAOImpl(conn);

            // 1. Insert
            System.out.println("\n--- 1. INSERT DATA ---");
            Product pNew = new Product("PG-01", "Bibit Durian Musang King", 150000, 10);
            dao.insert(pNew);

            // 2. Read
            System.out.println("\n--- 2. READ ALL DATA ---");
            List<Product> all = dao.findAll();
            for (Product p : all) {
                System.out.println("- " + p.getCode() + " | " + p.getName() + " | Rp" + p.getPrice());
            }

            // 3. Update
            System.out.println("\n--- 3. UPDATE DATA ---");
            Product pEdit = dao.findByCode("PG-01");
            if (pEdit != null) {
                pEdit.setStock(5); // Stok berkurang
                dao.update(pEdit);
            }

            // 4. Delete
            System.out.println("\n--- 4. DELETE DATA ---");
            dao.delete("PG-01");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal koneksi! Cek url, user, password PostgreSQL Anda.");
        }
    }
}