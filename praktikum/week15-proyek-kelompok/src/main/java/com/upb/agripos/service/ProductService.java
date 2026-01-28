package com.upb.agripos.service;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static ProductService instance;
    private ProductDAO dao;

    private ProductService() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            this.dao = new ProductDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public List<Product> getAllProducts() {
        try {
            if (dao != null) return dao.findAll();
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }
    
    public void addProduct(Product p) throws Exception {
        if (dao == null) throw new Exception("Koneksi Database Gagal!");
        dao.insert(p);
    }
    
    public void deleteProduct(String code) throws Exception {
        if (dao == null) throw new Exception("Koneksi Database Gagal!");
        dao.delete(code);
    }
}