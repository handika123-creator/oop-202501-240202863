package com.upb.agripos.service;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    private ProductDAO dao;

    public ProductService() {
        this.dao = new ProductDAOImpl();
    }

    public void addProduct(String code, String name, double price, int stock) throws Exception {
        if (price < 0) throw new Exception("Harga tidak boleh negatif!");
        if (stock < 0) throw new Exception("Stok tidak boleh negatif!");
        dao.insert(new Product(code, name, price, stock));
    }

    // --- TAMBAHAN WEEK 13 ---
    public void deleteProduct(String code) throws Exception {
        dao.delete(code);
    }

    public List<Product> getAllProducts() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}