package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    // Menambah produk baru ke database
    void insert(Product p) throws Exception;
    
    // Menghapus produk berdasarkan kode
    void delete(String code) throws Exception;
    
    // Mengambil semua data produk dari database
    List<Product> findAll() throws Exception;
}