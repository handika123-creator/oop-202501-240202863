package com.upb.agripos;

import com.upb.agripos.controller.ProductController;
import com.upb.agripos.view.ProductFormView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Inisialisasi VIEW (Tampilan)
        // Kita membuat objek tampilan kosong dulu
        ProductFormView view = new ProductFormView();
        
        // 2. Inisialisasi CONTROLLER (Otak Aplikasi)
        // Kita memasukkan 'view' ke dalam Controller agar Controller bisa mengendalikannya.
        // Saat baris ini jalan, Controller langsung memasang event handler ke tombol-tombol di View.
        new ProductController(view);

        // 3. Menyiapkan SCENE (Isi Jendela)
        // Memasukkan view kita ke dalam Scene dengan ukuran 400x500 pixel
        Scene scene = new Scene(view, 400, 500);

        // 4. Menyiapkan STAGE (Bingkai Jendela Utama)
        stage.setTitle("Agri-POS Week 12 - Handika (240202863)"); // Judul window
        stage.setScene(scene); // Pasang isinya
        stage.show(); // Tampilkan ke layar!
    }

    // Main Method standar Java untuk menjalankan aplikasi
    public static void main(String[] args) {
        launch(args); // Perintah khusus untuk memulai JavaFX
    }
}