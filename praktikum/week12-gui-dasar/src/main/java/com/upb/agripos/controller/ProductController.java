package com.upb.agripos.controller;

import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductFormView;
import javafx.scene.control.Alert;

public class ProductController {
    private final ProductService service;
    private final ProductFormView view;

    public ProductController(ProductFormView view) {
        this.view = view;
        this.service = new ProductService();
        
        // 1. Load data awal
        refreshData();
        
        // 2. Hubungkan Tombol dengan Method
        this.view.getBtnAdd().setOnAction(e -> tambahProduk());
    }

    private void tambahProduk() {
        try {
            String code = view.getTxtCode().getText();
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());

            service.addProduct(code, name, price, stock);

            refreshData();
            view.clearFields();
            showAlert("Sukses", "Produk berhasil ditambahkan!");

        } catch (NumberFormatException ex) {
            showAlert("Error", "Harga dan Stok harus angka!");
        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private void refreshData() {
        view.getListView().getItems().clear();
        try {
            service.getAllProducts().forEach(p -> 
                view.getListView().getItems().add(p.getCode() + " - " + p.getName() + " (Stok: " + p.getStock() + ")")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}