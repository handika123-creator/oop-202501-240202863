package com.upb.agripos.dao;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection conn = DatabaseConnection.getConnection();

    public void saveTransaction(double total, String paymentMethod, String cashier, List<CartItem> items) throws Exception {
        try {
            conn.setAutoCommit(false); // Mulai Transaksi

            // 1. Simpan Header
            String sqlHeader = "INSERT INTO transactions (total_amount, payment_method, cashier_username) VALUES (?, ?, ?) RETURNING id";
            PreparedStatement psHeader = conn.prepareStatement(sqlHeader);
            psHeader.setDouble(1, total);
            psHeader.setString(2, paymentMethod);
            psHeader.setString(3, cashier);
            
            ResultSet rs = psHeader.executeQuery();
            if (!rs.next()) throw new Exception("Gagal menyimpan header transaksi");
            int transId = rs.getInt(1);

            // 2. Simpan Detail & Update Stok
            String sqlDetail = "INSERT INTO transaction_details (transaction_id, product_code, quantity, subtotal) VALUES (?, ?, ?, ?)";
            String sqlStock = "UPDATE products SET stock = stock - ? WHERE code = ?";
            
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            PreparedStatement psStock = conn.prepareStatement(sqlStock);

            for (CartItem item : items) {
                // Detail
                psDetail.setInt(1, transId);
                psDetail.setString(2, item.getProduct().getCode());
                psDetail.setInt(3, item.getQuantity());
                psDetail.setDouble(4, item.getSubtotal());
                psDetail.addBatch();

                // Stok
                psStock.setInt(1, item.getQuantity());
                psStock.setString(2, item.getProduct().getCode());
                psStock.addBatch();
            }

            psDetail.executeBatch();
            psStock.executeBatch();

            conn.commit(); // Simpan permanen

        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace(); // Cek error di terminal jika gagal
            throw new Exception("Transaksi Gagal: " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        // Pastikan nama kolom sesuai dengan database
        String sql = "SELECT * FROM transactions ORDER BY date DESC";
        
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Transaction(
                    rs.getInt("id"),
                    rs.getTimestamp("date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method"),
                    rs.getString("cashier_username")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}