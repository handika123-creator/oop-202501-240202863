package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.User;
import com.upb.agripos.service.TransactionService;
import com.upb.agripos.service.payment.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PosView extends BorderPane {
    private final PosController controller = new PosController();
    private final TransactionService transactionService = new TransactionService();
    
    // Komponen GUI
    private TableView<Product> table = new TableView<>();
    private ListView<CartItem> cartList = new ListView<>();
    private Label lblTotal = new Label("Total: Rp 0");
    
    // Input Fields (Agar tombol Tambah berfungsi)
    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    
    private User currentUser;

    public PosView(User user, Stage stage) {
        this.currentUser = user;
        setPadding(new Insets(15));
        
        // --- HEADER ATAS ---
        // --- HEADER ATAS ---
HBox topBar = new HBox(10);
topBar.setPadding(new Insets(0, 0, 10, 0));

Label lblUser = new Label("Halo, " + user.getUsername() + " (" + user.getRole() + ")");
lblUser.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

// Tombol 1: Laporan (BARU)
Button btnReport = new Button("Lihat Laporan");
btnReport.setOnAction(e -> stage.setScene(new Scene(new ReportView(user, stage), 800, 500)));

// Tombol 2: Logout
Button btnLogout = new Button("Logout");
btnLogout.setStyle("-fx-background-color: #ffcccc;");
btnLogout.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 400, 300)));

topBar.getChildren().addAll(lblUser, new Separator(), btnReport, btnLogout);
setTop(topBar);

        // --- BAGIAN KIRI: Inventory & Form Input ---
        VBox left = new VBox(10); 
        left.setPrefWidth(600);
        
        // 1. Tabel Produk
        setupTable();

        // 2. Form Input (Hanya muncul untuk ADMIN)
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10); inputGrid.setVgap(10);
        inputGrid.addRow(0, new Label("Kode:"), txtCode, new Label("Nama:"), txtName);
        inputGrid.addRow(1, new Label("Harga:"), txtPrice, new Label("Stok:"), txtStock);
        
        // 3. Tombol CRUD
        Button btnAddDb = new Button("Simpan Baru");
        Button btnDelDb = new Button("Hapus Terpilih");
        Button btnToCart = new Button("Masuk Keranjang >>");
        btnToCart.setStyle("-fx-font-weight: bold; -fx-background-color: #ccffcc;");

        HBox adminBox = new HBox(10, btnAddDb, btnDelDb);
        
        // Proteksi: Sembunyikan fitur Admin jika login sebagai Kasir
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            inputGrid.setVisible(false); inputGrid.setManaged(false);
            adminBox.setVisible(false); adminBox.setManaged(false);
        }

        left.getChildren().addAll(new Label("INVENTORY BARANG"), table, inputGrid, adminBox, new Separator(), btnToCart);

        // --- BAGIAN KANAN: Keranjang Belanja ---
        VBox right = new VBox(10); 
        right.setPrefWidth(300);
        right.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 0 1; -fx-padding: 10;");
        
        Button btnPay = new Button("Bayar (Checkout)");
        btnPay.setMaxWidth(Double.MAX_VALUE);
        btnPay.setStyle("-fx-font-size: 14px; -fx-background-color: #3399ff; -fx-text-fill: white;");
        
        right.getChildren().addAll(new Label("KERANJANG BELANJA"), cartList, lblTotal, btnPay);

        setCenter(left); 
        setRight(right);
        
        // Load data awal dari database
        refreshData();

        // --- LOGIKA TOMBOL (EVENT HANDLERS) ---
        
        // 1. Logika Tambah Produk ke Database
        btnAddDb.setOnAction(e -> {
            try {
                controller.addProductToDb(
                    txtCode.getText(), 
                    txtName.getText(), 
                    Double.parseDouble(txtPrice.getText()), 
                    Integer.parseInt(txtStock.getText())
                );
                refreshData(); 
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk berhasil disimpan!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal simpan: " + ex.getMessage());
            }
        });

        // 2. Logika Hapus Produk dari Database
        btnDelDb.setOnAction(e -> {
            Product p = table.getSelectionModel().getSelectedItem();
            if (p != null) {
                try {
                    controller.deleteProduct(p.getCode());
                    refreshData();
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk dihapus!");
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih produk di tabel dulu!");
            }
        });

        // 3. Logika Masuk Keranjang
        btnToCart.setOnAction(e -> {
            Product p = table.getSelectionModel().getSelectedItem();
            if (p != null) {
                if (p.getStock() > 0) {
                    controller.addToCart(p, 1);
                    refreshCart();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Stok Habis", "Produk ini stoknya 0!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Pilih Produk", "Klik salah satu produk di tabel kiri.");
            }
        });

        // 4. Logika Bayar
        btnPay.setOnAction(e -> showPaymentDialog());
    }

    // --- HELPER METHODS ---

    private void showPaymentDialog() {
        if (controller.getCartTotal() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Keranjang Kosong", "Belum ada barang yang dibeli.");
            return;
        }

        ChoiceDialog<PaymentStrategy> dialog = new ChoiceDialog<>(new CashPayment(), new CashPayment(), new EWalletPayment());
        dialog.setTitle("Pembayaran");
        dialog.setHeaderText("Total Tagihan: Rp " + controller.getCartTotal());
        dialog.setContentText("Pilih Metode Bayar:");
        
        dialog.showAndWait().ifPresent(method -> {
            try {
                transactionService.processCheckout(currentUser, controller.getCartItems(), controller.getCartTotal(), method);
                controller.checkout(); // Kosongkan keranjang
                refreshCart(); 
                refreshData(); // Refresh stok di tabel
                showAlert(Alert.AlertType.INFORMATION, "Transaksi Berhasil", "Pembayaran sukses & Stok berkurang.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Transaksi Gagal", ex.getMessage());
            }
        });
    }

    private void setupTable() {
        TableColumn<Product, String> cCode = new TableColumn<>("Kode");
        cCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        TableColumn<Product, String> cName = new TableColumn<>("Nama");
        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cName.setMinWidth(150);
        
        TableColumn<Product, Double> cPrice = new TableColumn<>("Harga");
        cPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Product, Integer> cStock = new TableColumn<>("Stok");
        cStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().clear();
        table.getColumns().addAll(cCode, cName, cPrice, cStock);
    }

    private void refreshData() { table.setItems(controller.loadProducts()); }
    
    private void refreshCart() { 
        cartList.setItems(controller.getCartItems()); 
        lblTotal.setText("Total: Rp " + controller.getCartTotal());
    }
    
    private void clearFields() { 
        txtCode.clear(); txtName.clear(); txtPrice.clear(); txtStock.clear(); 
    }
    
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}