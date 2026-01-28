package com.upb.agripos.service;

import com.upb.agripos.dao.TransactionDAO;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.User;
import com.upb.agripos.service.payment.PaymentStrategy;
import java.util.List;

public class TransactionService {
    private TransactionDAO dao = new TransactionDAO();

    // Logic Checkout
    public void processCheckout(User cashier, List<CartItem> items, double total, PaymentStrategy method) throws Exception {
        if (items.isEmpty()) throw new Exception("Keranjang belanja kosong!");
        
        // 1. Eksekusi Pembayaran
        method.pay(total);

        // 2. Simpan ke Database
        // Pastikan DAO memiliki method saveTransaction
        dao.saveTransaction(total, method.getMethodName(), cashier.getUsername(), items);
    }

    // Logic Report
    public List<Transaction> getSalesReport() {
        return dao.findAll();
    }
}