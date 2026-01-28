package com.upb.agripos.view;

import com.upb.agripos.dao.UserDAO;
import com.upb.agripos.model.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView extends VBox {
    public LoginView(Stage stage) {
        setAlignment(Pos.CENTER); 
        setSpacing(10);
        
        Label lblTitle = new Label("AGRI-POS SYSTEM");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField userF = new TextField(); 
        userF.setPromptText("Username (admin/kasir)"); 
        userF.setMaxWidth(200);
        
        PasswordField passF = new PasswordField(); 
        passF.setPromptText("Password"); 
        passF.setMaxWidth(200);
        
        Button btn = new Button("LOGIN");
        
        btn.setOnAction(e -> {
            try {
                // 1. Cek Login ke Database
                UserDAO dao = new UserDAO();
                User u = dao.login(userF.getText(), passF.getText());
                
                if (u != null) {
                    // 2. Jika Login Sukses, Coba Buka Halaman Utama
                    try {
                        System.out.println("Login Sukses, membuka PosView...");
                        PosView view = new PosView(u, stage); // Rawan Error di sini
                        Scene scene = new Scene(view, 900, 600);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                    } catch (Throwable ex) {
                        // TANGKAP ERROR HALAMAN UTAMA
                        ex.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Gagal Memuat Halaman");
                        alert.setHeaderText("Terjadi Error di PosView!");
                        alert.setContentText("Penyebab: " + ex.toString());
                        alert.showAndWait();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Username/Password Salah!").show();
                }
            } catch (Exception ex) {
                // TANGKAP ERROR DATABASE
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error Database: " + ex.getMessage()).show();
            }
        });
        
        getChildren().addAll(lblTitle, userF, passF, btn);
    }
}