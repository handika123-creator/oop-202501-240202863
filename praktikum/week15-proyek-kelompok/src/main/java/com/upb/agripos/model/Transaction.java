package com.upb.agripos.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Transaction {
    private int id;
    private Timestamp date;
    private double amount;
    private String paymentMethod;
    private String cashierName;

    // Konstruktor
    public Transaction(int id, Timestamp date, double amount, String paymentMethod, String cashierName) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.cashierName = cashierName;
    }

    // Getters (Wajib ada untuk PropertyValueFactory di TableView)
    public int getId() { return id; }
    
    // Format tanggal agar rapi di tabel
    public String getDate() { 
        if (date == null) return "-";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date); 
    }
    
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getCashierName() { return cashierName; }
}