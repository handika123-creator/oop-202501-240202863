package com.upb.agripos.controller;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.PosView;
import javafx.scene.control.Alert;

public class PosController {
    private PosView view;
    private ProductService productService;
    private CartService cartService;

    public PosController(PosView view) {
        this.view = view;
        this.productService = new ProductService();
        this.cartService = new CartService();

        initController();
    }

    private void initController() {
        refreshProductTable();

        // Event Handlers
        view.getBtnAddProduct().setOnAction(e -> addProduct());
        view.getBtnDeleteProduct().setOnAction(e -> deleteProduct());
        view.getBtnAddToCart().setOnAction(e -> addToCart());
        view.getBtnCheckout().setOnAction(e -> checkout());
    }

    // --- FITUR PRODUK ---
    private void refreshProductTable() {
        view.getProductTable().getItems().clear();
        view.getProductTable().getItems().addAll(productService.getAllProducts());
    }

    private void addProduct() {
        try {
            String code = view.getTxtCode().getText();
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());

            productService.addProduct(code, name, price, stock);
            refreshProductTable();
            view.clearForm();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void deleteProduct() {
        Product selected = view.getProductTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                productService.deleteProduct(selected.getCode());
                refreshProductTable();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        }
    }

    // --- FITUR KERANJANG ---
    private void addToCart() {
        Product selected = view.getProductTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Default tambah 1 pcs setiap klik
                cartService.addToCart(selected, 1);
                updateCartView();
            } catch (Exception e) {
                showAlert("Gagal Masuk Keranjang", e.getMessage());
            }
        } else {
            showAlert("Info", "Pilih produk di tabel dulu!");
        }
    }

    private void updateCartView() {
        view.getCartListView().getItems().clear();
        for (CartItem item : cartService.getCartItems()) {
            String text = item.getProduct().getName() + " x" + item.getQuantity() + 
                          " = Rp " + item.getSubtotal();
            view.getCartListView().getItems().add(text);
        }
        view.getLblTotal().setText("Total: Rp " + cartService.calculateTotal());
    }
    
    private void checkout() {
        cartService.clearCart();
        updateCartView();
        showAlert("Sukses", "Transaksi Selesai! (Simulasi)");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}