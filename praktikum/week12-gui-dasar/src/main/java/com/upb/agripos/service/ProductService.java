package com.upb.agripos.service;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    private ProductDAO dao;

    public ProductService() {
        this.dao = new ProductDAOImpl(DatabaseConnection.getConnection());
    }

    public void addProduct(String code, String name, double price, int stock) throws Exception {
        if (price < 0) throw new Exception("Harga tidak boleh negatif!");
        dao.insert(new Product(code, name, price, stock));
    }

    public List<Product> getAllProducts() throws Exception {
        return dao.findAll();
    }
}