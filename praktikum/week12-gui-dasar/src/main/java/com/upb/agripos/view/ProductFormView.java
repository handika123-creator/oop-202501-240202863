package com.upb.agripos.view;

// Import library JavaFX (Penyebab merah kalau pom.xml belum beres)
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ProductFormView extends VBox {
    // 1. Deklarasi Komponen UI (Form Input)
    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    
    // Tombol aksi
    private Button btnAdd = new Button("Tambah Produk");
    
    // Area untuk menampilkan data (List)
    private ListView<String> listView = new ListView<>();

    // Constructor: Dipanggil saat aplikasi pertama kali dijalankan
    public ProductFormView() {
        setupUI();
    }

    // Method untuk menyusun tata letak (Layout)
    private void setupUI() {
        // Atur jarak pinggir (Padding) dan jarak antar elemen (Spacing)
        this.setPadding(new Insets(20));
        this.setSpacing(15);

        // Judul Aplikasi
        Label title = new Label("Input Produk Agri-POS");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Membuat Grid untuk Form (Baris & Kolom rapi)
        GridPane grid = new GridPane();
        grid.setHgap(10); // Jarak horizontal
        grid.setVgap(10); // Jarak vertikal
        
        // Menyusun Label dan TextField ke dalam Grid
        grid.addRow(0, new Label("Kode Produk:"), txtCode);
        grid.addRow(1, new Label("Nama Produk:"), txtName);
        grid.addRow(2, new Label("Harga:"), txtPrice);
        grid.addRow(3, new Label("Stok:"), txtStock);
        
        // Menambahkan tombol di baris ke-4, kolom ke-1
        grid.add(btnAdd, 1, 4);

        // Memasukkan semua elemen ke dalam VBox (Container Utama)
        this.getChildren().addAll(
            title, 
            grid, 
            new Separator(),           // Garis pembatas
            new Label("Daftar Produk:"), 
            listView
        );
    }

    // ---------------------------------------------------------
    // GETTER METHODS (PENTING!)
    // Agar Controller bisa membaca apa yang diketik user,
    // dan agar Controller bisa memasang aksi di tombol.
    // ---------------------------------------------------------
    public TextField getTxtCode() { return txtCode; }
    public TextField getTxtName() { return txtName; }
    public TextField getTxtPrice() { return txtPrice; }
    public TextField getTxtStock() { return txtStock; }
    public Button getBtnAdd() { return btnAdd; }
    public ListView<String> getListView() { return listView; }

    // Utility: Membersihkan form setelah simpan berhasil
    public void clearFields() {
        txtCode.clear();
        txtName.clear();
        txtPrice.clear();
        txtStock.clear();
        // Fokuskan kursor kembali ke input pertama
        txtCode.requestFocus();
    }
}