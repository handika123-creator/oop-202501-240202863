package main.java.com.upb.agripos.controller;

import main.java.com.upb.agripos.model.Product;
import main.java.com.upb.agripos.view.ConsoleView;

public class ProductController {
    private final Product model;
    private final ConsoleView view;

    public ProductController(Product model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    public void showProduct() {
        // Controller mengambil data dari Model
        String detail = "Produk: " + model.getCode() + " - " + model.getName();
        // Controller mengirim data ke View untuk ditampilkan
        view.showMessage(detail);
    }
}