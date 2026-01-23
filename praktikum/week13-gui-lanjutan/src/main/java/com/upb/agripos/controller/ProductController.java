package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductTableView; // <-- IMPORT YANG BARU
import javafx.scene.control.Alert;

public class ProductController {
    private final ProductService service;
    private final ProductTableView view; // <-- GANTI TIPE DATA INI

    // GANTI PARAMETER DI CONSTRUCTOR
    public ProductController(ProductTableView view) {
        this.view = view;
        this.service = new ProductService();
        
        refreshTable();

        this.view.getBtnAdd().setOnAction(e -> handleAddProduct());
        this.view.getBtnDelete().setOnAction(e -> handleDeleteProduct());
    }

    // ... (SISA KODE KE BAWAH SAMA PERSIS SEPERTI SEBELUMNYA) ...
    
    private void handleAddProduct() {
        try {
            String code = view.getTxtCode().getText();
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());

            service.addProduct(code, name, price, stock);
            refreshTable();
            view.clearFields();
            showAlert("Sukses", "Data berhasil disimpan!");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void handleDeleteProduct() {
        Product selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.deleteProduct(selected.getCode());
                refreshTable();
                showAlert("Sukses", "Data dihapus!");
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        } else {
            showAlert("Warning", "Pilih data dulu!");
        }
    }

    private void refreshTable() {
        view.getTable().getItems().clear();
        view.getTable().getItems().addAll(service.getAllProducts());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}