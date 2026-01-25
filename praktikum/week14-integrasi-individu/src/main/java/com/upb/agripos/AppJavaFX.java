package com.upb.agripos;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.view.PosView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // Syarat Bab 1: Identitas di Console
        System.out.println("Hello World, I am HANDIKA DWI ARDIYANTO-240202863");

        PosView view = new PosView();
        new PosController(view);

        Scene scene = new Scene(view, 800, 600); // Lebar window diperbesar
        stage.setTitle("Agri-POS Week 14 - Handika");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}