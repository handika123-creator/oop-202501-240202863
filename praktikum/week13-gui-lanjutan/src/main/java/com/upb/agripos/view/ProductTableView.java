package com.upb.agripos.view;

import com.upb.agripos.model.Product;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// GANTI NAMA CLASS JADI ProductTableView
public class ProductTableView extends VBox { 
    // Komponen Input
    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    
    // Tombol
    private Button btnAdd = new Button("Tambah Produk");
    private Button btnDelete = new Button("Hapus Produk");

    // Tabel
    private TableView<Product> table = new TableView<>();

    // GANTI NAMA CONSTRUCTOR
    public ProductTableView() {
        setupUI();
    }

    private void setupUI() {
        this.setPadding(new Insets(20));
        this.setSpacing(10);
        
        // Judul disesuaikan
        Label lblTitle = new Label("Manajemen Produk (TableView)");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout Form
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Kode:"), txtCode);
        grid.addRow(1, new Label("Nama:"), txtName);
        grid.addRow(2, new Label("Harga:"), txtPrice);
        grid.addRow(3, new Label("Stok:"), txtStock);

        HBox buttonBox = new HBox(10, btnAdd, btnDelete);

        setupTableColumns();

        this.getChildren().addAll(lblTitle, grid, buttonBox, new Label("Daftar Produk:"), table);
    }

    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<Product, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(new PropertyValueFactory<>("code")); 

        TableColumn<Product, String> colName = new TableColumn<>("Nama");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setMinWidth(150);

        TableColumn<Product, Double> colPrice = new TableColumn<>("Harga");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> colStock = new TableColumn<>("Stok");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(colCode, colName, colPrice, colStock);
    }

    // Getters
    public TextField getTxtCode() { return txtCode; }
    public TextField getTxtName() { return txtName; }
    public TextField getTxtPrice() { return txtPrice; }
    public TextField getTxtStock() { return txtStock; }
    public Button getBtnAdd() { return btnAdd; }
    public Button getBtnDelete() { return btnDelete; }
    public TableView<Product> getTable() { return table; }

    public void clearFields() {
        txtCode.clear(); txtName.clear(); txtPrice.clear(); txtStock.clear();
        txtCode.requestFocus();
    }
}