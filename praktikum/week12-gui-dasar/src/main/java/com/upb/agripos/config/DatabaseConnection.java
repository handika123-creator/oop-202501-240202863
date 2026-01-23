package com.upb.agripos.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    // Pengaturan Database PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private static final String USER = "postgres";
    // ⚠️ PENTING: Ganti "your_password" dengan password pgAdmin kamu yang asli!
    private static final String PASS = "1234"; 

    /**
     * Method statis untuk mengambil objek Connection.
     * Dapat dipanggil dari mana saja dengan: DatabaseConnection.getConnection()
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Memanggil Driver PostgreSQL (opsional di Java terbaru, tapi bagus untuk memastikan)
            Class.forName("org.postgresql.Driver");
            
            // Membuat koneksi
            conn = DriverManager.getConnection(URL, USER, PASS);
            // System.out.println("Koneksi Database Berhasil!"); // Un-comment jika ingin testing
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL tidak ditemukan! Cek pom.xml.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Gagal terhubung ke Database: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}