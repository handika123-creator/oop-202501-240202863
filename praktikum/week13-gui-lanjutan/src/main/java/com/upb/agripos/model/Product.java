package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    // Constructor Kosong (Opsional tapi disarankan ada)
    public Product() {
    }

    // Constructor Lengkap
    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // --- GETTER METHODS (PENTING UNTUK TABLEVIEW) ---
    // Tanpa Getter ini, tabel akan KOSONG/BLANK.
    // Nama getter harus sesuai pola: get + NamaVariabel (Huruf depan besar)
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // --- SETTER METHODS ---
    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}