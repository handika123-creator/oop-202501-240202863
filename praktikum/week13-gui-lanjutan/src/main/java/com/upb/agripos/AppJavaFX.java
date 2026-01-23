package com.upb.agripos;

import com.upb.agripos.controller.ProductController;
import com.upb.agripos.view.ProductTableView; // <-- GANTI IMPORT
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // GANTI OBJECT JADI ProductTableView
        ProductTableView view = new ProductTableView();
        
        new ProductController(view);

        Scene scene = new Scene(view, 600, 500);
        stage.setTitle("Agri-POS Week 13 - Handika");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}