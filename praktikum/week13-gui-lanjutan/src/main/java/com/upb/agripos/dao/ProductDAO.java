package com.upb.agripos.dao;
import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    void insert(Product p) throws Exception;
    void delete(String code) throws Exception; // <-- TAMBAHAN WEEK 13
    List<Product> findAll() throws Exception;
}