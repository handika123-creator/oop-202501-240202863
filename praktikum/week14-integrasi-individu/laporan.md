# Laporan Praktikum Minggu 14
Topik: Integrasi Individu (Agri-POS: OOP + Database + GUI)

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
1. Mengintegrasikan konsep OOP, GUI (JavaFX), dan Database (JDBC) menjadi satu aplikasi utuh.
2. Menerapkan arsitektur MVC (Model-View-Controller) dan DAO (Data Access Object) sesuai prinsip SOLID.
3. Mengimplementasikan fitur Keranjang Belanja menggunakan Collections.
4. Melakukan Unit Testing menggunakan JUnit 5.
---

## Dasar Teori
1. Integrasi Sistem: Menggabungkan berbagai modul (GUI, Logika Bisnis, Database) agar bekerja sama dalam satu alur kerja yang runtut.
2. DAO Pattern (Data Access Object): Pola desain yang memisahkan logika bisnis dari logika akses data, sehingga aplikasi lebih mudah dirawat dan database bisa diganti tanpa merombak seluruh kode.
3. Unit Testing (JUnit): Metode pengujian perangkat lunak di mana unit terkecil dari kode (seperti method atau class) diuji secara terisolasi untuk memastikan fungsinya berjalan benar sebelum diintegrasikan.
4. Collections: Struktur data di Java (seperti List, Map) yang digunakan untuk menyimpan sekumpulan objek sementara di memori, contohnya untuk menampung item di keranjang belanja.

---

## Langkah Praktikum
1. Setup Project: Menyiapkan struktur folder Maven dan menambahkan dependensi postgresql, javafx, dan junit-jupiter pada pom.xml.
2. Database: Membuat database agripos dan tabel products di PostgreSQL.
3. Coding (Model & DAO): Membuat class Product dan CartItem, serta mengimplementasikan JdbcProductDAO untuk koneksi database.
4. Coding (Service & Logic): Membuat ProductService untuk validasi dan CartService untuk logika keranjang belanja.
5. Coding (View & Controller): Membuat tampilan GUI dengan JavaFX (PosView) dan menghubungkannya lewat PosController.
6. Testing: Membuat unit test CartServiceTest di folder src/test/java untuk menguji perhitungan total belanja.
7. Integrasi: Menghubungkan tombol GUI dengan Service dan DAO.
8. Commit Message: week14-integrasi-individu: [fitur] selesai integrasi full stack

---

## Kode Program
PosController.java
```java
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
```
JdbcProductDAO.java
```java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDAO implements ProductDAO {
    private Connection conn;

    // --- PENTING: Ganti Password PostgreSQL di sini ---
    private final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private final String USER = "postgres";
    private final String PASS = "your_password"; // <--- GANTI INI
    // -------------------------------------------------

    public JdbcProductDAO() {
        try {
            // Membuka koneksi ke Database PostgreSQL
            // Pastikan driver sudah terinstall di pom.xml
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Gagal koneksi database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Product p) throws Exception {
        cekKoneksi();
        String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String code) throws Exception {
        cekKoneksi();
        String sql = "DELETE FROM products WHERE code = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        cekKoneksi();
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY code ASC";
        
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        }
        return list;
    }

    // Method bantuan untuk memastikan koneksi aktif
    private void cekKoneksi() throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Koneksi Database Terputus atau Belum Terbuka!");
        }
    }
}
```
ProductDAO.java
```java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    // Menambah produk baru ke database
    void insert(Product p) throws Exception;
    
    // Menghapus produk berdasarkan kode
    void delete(String code) throws Exception;
    
    // Mengambil semua data produk dari database
    List<Product> findAll() throws Exception;
}
```
Cart.java
```java
package com.upb.agripos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    // Menggunakan Map<String, CartItem> agar mudah cari berdasarkan Kode Produk
    private Map<String, CartItem> items = new HashMap<>();

    public void addItem(Product product, int qty) {
        if (items.containsKey(product.getCode())) {
            // Jika sudah ada, update jumlahnya
            items.get(product.getCode()).addQuantity(qty);
        } else {
            // Jika belum ada, tambah baru
            items.put(product.getCode(), new CartItem(product, qty));
        }
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public double getTotalPrice() {
        return items.values().stream()
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
    }
}
```
Curtitem.java
```java
package com.upb.agripos.model;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    
    // Menghitung subtotal (Harga x Jumlah)
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
    }
}
```
Product.java
```java
package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    // Constructor Kosong (Wajib untuk beberapa library Java)
    public Product() {
    }

    // Constructor Lengkap (Untuk kemudahan saat membuat objek baru)
    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // --- GETTER METHODS (WAJIB ADA untuk TableView) ---
    // TableView menggunakan "PropertyValueFactory" yang mencari method ini.
    // Misal: new PropertyValueFactory<>("code") akan mencari getCode()
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // --- SETTER METHODS (Opsional, tapi bagus untuk update data) ---
    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
```
Cartservice.java
```java
package com.upb.agripos.service;

import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import java.util.List;

public class CartService {
    private Cart cart = new Cart();

    public void addToCart(Product product, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Jumlah harus lebih dari 0!");
        }
        if (product.getStock() < quantity) {
            throw new Exception("Stok tidak cukup!");
        }
        cart.addItem(product, quantity);
    }

    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    public double calculateTotal() {
        return cart.getTotalPrice();
    }

    public void clearCart() {
        cart.clear();
    }
}
```
ProductService.java
```java
package com.upb.agripos.service;

import com.upb.agripos.dao.JdbcProductDAO;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    // Kita gunakan Interface 'ProductDAO' sebagai tipe datanya (Polymorphism)
    // Tapi aslinya yang kita pakai adalah 'JdbcProductDAO'
    private ProductDAO dao;

    public ProductService() {
        // --- PENTING WEEK 14 ---
        // Pastikan ini memanggil JdbcProductDAO (bukan ProductDAOImpl lagi)
        this.dao = new JdbcProductDAO();
    }

    public void addProduct(String code, String name, double price, int stock) throws Exception {
        // --- VALIDASI LOGIKA BISNIS (Business Logic) ---
        // Service bertugas menyaring data "sampah" sebelum masuk ke DAO
        
        if (code == null || code.trim().isEmpty()) {
            throw new Exception("Kode produk tidak boleh kosong!");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Nama produk tidak boleh kosong!");
        }
        if (price < 0) {
            throw new Exception("Harga tidak boleh negatif!");
        }
        if (stock < 0) {
            throw new Exception("Stok tidak boleh negatif!");
        }

        // Jika lolos validasi, baru kirim ke DAO
        dao.insert(new Product(code, name, price, stock));
    }

    public void deleteProduct(String code) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new Exception("Kode produk tidak valid!");
        }
        dao.delete(code);
    }

    public List<Product> getAllProducts() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            // Jika error database, kembalikan list kosong agar aplikasi tidak crash
            return List.of(); 
        }
    }
}
```
PosView.java
```java
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
```
AppJavaFX.java
```java
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
```
Launcher.java
```java
package com.upb.agripos;

public class Launcher {
    public static void main(String[] args) {
        // Memanggil main method yang asli di AppJavaFX
        AppJavaFX.main(args);
    }
}
```
CartServiceTest.java
```java
package com.upb.agripos;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CartServiceTest {

    @Test
    public void testCalculateTotal() {
        try {
            CartService cartService = new CartService();
            Product p1 = new Product("A1", "Test1", 10000, 10);
            Product p2 = new Product("A2", "Test2", 5000, 10);

            cartService.addToCart(p1, 2); // 20.000
            cartService.addToCart(p2, 1); // 5.000

            double total = cartService.calculateTotal();
            Assertions.assertEquals(25000.0, total, "Total harusnya 25.000");
        } catch (Exception e) {
            Assertions.fail("Exception tidak boleh terjadi: " + e.getMessage());
        }
    }
}
```
Pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.upb</groupId>
    <artifactId>agripos-integrasi-individu</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <javafx.version>21</javafx.version>
        <junit.version>5.10.0</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.6.0</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.upb.agripos.AppJavaFX</mainClass>
                </configuration>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Hasil Eksekusi

![Screenshot hasil](/praktikum/week14-integrasi-individu/screenshots/app_main.png)
![Screenshot hasil](/praktikum/week14-integrasi-individu/screenshots/junit_result.png)
---

## Analisis
1. Cara Kerja Kode: Aplikasi berjalan dengan alur MVC. Saat pengguna menekan tombol (View), PosController menangkap aksi tersebut. Controller kemudian memanggil Service untuk validasi logika. Jika butuh data database, Service memanggil DAO untuk eksekusi query SQL (JDBC). Jika butuh data keranjang, Service memanipulasi Collections di memori.
2. Tabel Traceability (Desain Bab 6 vs Implementasi):

   | Artefak Desain | Referensi (ID) | Handler / Trigger GUI | Controller / Service | DAO / Data Access | Dampak UI / DB |
   | :--- | :--- | :--- | :--- | :--- | :--- |
   | **Use Case** | UC-01 Lihat Produk | Aplikasi Start / `loadData()` | `PosController.refresh()` → `ProductService.getAll()` | `JdbcProductDAO.findAll()` | Data dari DB muncul di TableView |
   | **Use Case** | UC-02 Tambah Produk | Tombol "Tambah Produk" | `PosController.addProduct()` → `ProductService.addProduct()` | `JdbcProductDAO.insert()` | Data tersimpan di DB & Tabel Reload |
   | **Sequence** | SD-01 Hapus Produk | Tombol "Hapus Produk" | `PosController.deleteProduct()` → `ProductService.delete()` | `JdbcProductDAO.delete()` | Baris hilang dari DB & UI |
   | **Activity** | AD-01 Tambah Keranjang | Tombol "Masuk Keranjang >" | `PosController.addToCart()` → `CartService.addToCart()` | *(In-Memory Collection)* | Item muncul di List View Kanan |
3. Kendala & Solusi:
   - Kendala: Mengalami error "Main class isn't unique" dan file berwarna kuning saat dijalankan. Juga error pada JUnit test yang tidak terdeteksi.
   - Analisis Masalah: Error terjadi karena membuka folder induk (praktikum) sehingga VS Code bingung membaca banyak file Launcher sekaligus. Selain itu, file CartServiceTest diletakkan di folder main, padahal seharusnya di folder test (sesuai aturan Maven).
   - Solusi: Menutup folder lama, lalu membuka spesifik folder week14-integrasi-individu. Memindahkan file test ke direktori src/test/java/com/upb/agripos.
---

## Kesimpulan
Praktikum minggu ini berhasil mengintegrasikan seluruh materi semester menjadi satu aplikasi utuh. Penggunaan arsitektur Layered (View-Controller-Service-DAO) terbukti membuat kode lebih rapi dan mudah di-debug. Kendala konfigurasi IDE mengajarkan pentingnya struktur direktori project yang standar (Maven Standard Directory Layout).

---