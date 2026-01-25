package com.upb.agripos.view;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class PosView extends BorderPane {
    // --- KIRI: Manajemen Produk ---
    private TableView<Product> productTable = new TableView<>();
    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    private Button btnAddProduct = new Button("Tambah Produk");
    private Button btnDeleteProduct = new Button("Hapus Produk");
    
    // --- KANAN: Keranjang Belanja ---
    private ListView<String> cartListView = new ListView<>();
    private Button btnAddToCart = new Button("Masuk Keranjang >");
    private Label lblTotal = new Label("Total: Rp 0");
    private Button btnCheckout = new Button("Bayar / Reset");

    public PosView() {
        setupUI();
    }

    private void setupUI() {
        setPadding(new Insets(15));
        
        // --- SETUP TABLE PRODUK (KIRI) ---
        setupProductTable();
        VBox formBox = new VBox(10, 
            new Label("Kode"), txtCode, 
            new Label("Nama"), txtName,
            new Label("Harga"), txtPrice,
            new Label("Stok"), txtStock,
            new HBox(10, btnAddProduct, btnDeleteProduct)
        );
        VBox leftPane = new VBox(10, new Label("DAFTAR PRODUK"), productTable, formBox);
        leftPane.setPrefWidth(400);

        // --- SETUP KERANJANG (KANAN) ---
        VBox rightPane = new VBox(10, 
            new Label("KERANJANG BELANJA"),
            btnAddToCart, 
            cartListView, 
            lblTotal,
            btnCheckout
        );
        rightPane.setPrefWidth(250);
        
        // --- GABUNGKAN ---
        setCenter(leftPane);
        setRight(rightPane);
    }

    @SuppressWarnings("unchecked")
    private void setupProductTable() {
        TableColumn<Product, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        TableColumn<Product, String> colName = new TableColumn<>("Nama");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, Double> colPrice = new TableColumn<>("Harga");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> colStock = new TableColumn<>("Stok");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        productTable.getColumns().addAll(colCode, colName, colPrice, colStock);
    }

    // Getters
    public TableView<Product> getProductTable() { return productTable; }
    public TextField getTxtCode() { return txtCode; }
    public TextField getTxtName() { return txtName; }
    public TextField getTxtPrice() { return txtPrice; }
    public TextField getTxtStock() { return txtStock; }
    public Button getBtnAddProduct() { return btnAddProduct; }
    public Button getBtnDeleteProduct() { return btnDeleteProduct; }
    public Button getBtnAddToCart() { return btnAddToCart; }
    public ListView<String> getCartListView() { return cartListView; }
    public Label getLblTotal() { return lblTotal; }
    public Button getBtnCheckout() { return btnCheckout; }
    
    public void clearForm() {
        txtCode.clear(); txtName.clear(); txtPrice.clear(); txtStock.clear();
    }
}