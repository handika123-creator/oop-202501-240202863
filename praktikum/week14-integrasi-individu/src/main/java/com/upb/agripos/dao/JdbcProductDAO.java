package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDAO implements ProductDAO {
    private Connection conn;

    // --- PENTING: Ganti Password PostgreSQL di sini ---
    private final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private final String USER = "postgres";
    private final String PASS = "your_password"; // <--- GANTI INI
    // -------------------------------------------------

    public JdbcProductDAO() {
        try {
            // Membuka koneksi ke Database PostgreSQL
            // Pastikan driver sudah terinstall di pom.xml
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Gagal koneksi database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Product p) throws Exception {
        cekKoneksi();
        String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String code) throws Exception {
        cekKoneksi();
        String sql = "DELETE FROM products WHERE code = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        cekKoneksi();
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY code ASC";
        
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        }
        return list;
    }

    // Method bantuan untuk memastikan koneksi aktif
    private void cekKoneksi() throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Koneksi Database Terputus atau Belum Terbuka!");
        }
    }
}