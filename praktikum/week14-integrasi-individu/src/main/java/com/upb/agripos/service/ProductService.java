package com.upb.agripos.service;

import com.upb.agripos.dao.JdbcProductDAO;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    // Kita gunakan Interface 'ProductDAO' sebagai tipe datanya (Polymorphism)
    // Tapi aslinya yang kita pakai adalah 'JdbcProductDAO'
    private ProductDAO dao;

    public ProductService() {
        // --- PENTING WEEK 14 ---
        // Pastikan ini memanggil JdbcProductDAO (bukan ProductDAOImpl lagi)
        this.dao = new JdbcProductDAO();
    }

    public void addProduct(String code, String name, double price, int stock) throws Exception {
        // --- VALIDASI LOGIKA BISNIS (Business Logic) ---
        // Service bertugas menyaring data "sampah" sebelum masuk ke DAO
        
        if (code == null || code.trim().isEmpty()) {
            throw new Exception("Kode produk tidak boleh kosong!");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Nama produk tidak boleh kosong!");
        }
        if (price < 0) {
            throw new Exception("Harga tidak boleh negatif!");
        }
        if (stock < 0) {
            throw new Exception("Stok tidak boleh negatif!");
        }

        // Jika lolos validasi, baru kirim ke DAO
        dao.insert(new Product(code, name, price, stock));
    }

    public void deleteProduct(String code) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new Exception("Kode produk tidak valid!");
        }
        dao.delete(code);
    }

    public List<Product> getAllProducts() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            // Jika error database, kembalikan list kosong agar aplikasi tidak crash
            return List.of(); 
        }
    }
}