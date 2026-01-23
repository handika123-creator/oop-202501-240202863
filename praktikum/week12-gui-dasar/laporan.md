# Laporan Praktikum Minggu 12
Topik: GUI DASAR JAVAFX & EVENT-DRIVEN PROGRAMMING

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
- Mahasiswa mampu menjelaskan konsep Event-Driven Programming.
- Mahasiswa mampu membangun antarmuka grafis (GUI) sederhana menggunakan JavaFX.
- Mahasiswa mampu menerapkan arsitektur MVC (Model-View-Controller) secara ketat.
- Mahasiswa mampu mengintegrasikan tampilan GUI dengan backend (DAO & Service) yang terhubung ke database PostgreSQL.

---

## Dasar Teori
1. JavaFX: Merupakan pustaka (library) Java yang digunakan untuk mengembangkan aplikasi desktop dengan antarmuka grafis modern (GUI).
2. Event-Driven Programming: Paradigma pemrograman di mana alur program ditentukan oleh aksi pengguna (seperti klik tombol atau ketikan keyboard), bukan oleh urutan kode yang prosedural.
3. MVC Pattern: Pola desain yang memisahkan aplikasi menjadi tiga komponen: Model (Data), View (Tampilan), dan Controller (Logika Penghubung), untuk memudahkan pemeliharaan kode.
4. Service Layer: Lapisan tambahan di antara Controller dan DAO untuk memastikan logika bisnis tervalidasi sebelum data masuk ke database.

---

## Langkah Praktikum
1. Konfigurasi Maven: Menambahkan dependensi javafx-controls dan postgresql pada file pom.xml serta melakukan reload project.
2. Setup Database: Membuat class DatabaseConnection.java di package config untuk koneksi ke PostgreSQL.
3. Implementasi Backend: Membuat Product.java (Model), ProductDAOImpl.java (Akses Data), dan ProductService.java (Logika Bisnis).
4. Implementasi Frontend (View): Membuat ProductFormView.java yang berisi komponen GUI seperti TextField, Button, dan ListView.
5. Implementasi Logic (Controller): Membuat ProductController.java untuk menangani event tombol "Tambah" dan menghubungkan View dengan Service.
6. Running: Menjalankan aplikasi melalui class utama AppJavaFX.java.

---

## Kode Program
```java
package main.java.com.upb.agripos.dao;

import java.util.List;
import main.java.com.upb.agripos.model.Product;

public interface ProductDAO {
    void insert(Product product) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;
    void update(Product product) throws Exception;
    void delete(String code) throws Exception;
}
```

```java
package com.upb.agripos.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    // Pengaturan Database PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private static final String USER = "postgres";
    // ⚠️ PENTING: Ganti "your_password" dengan password pgAdmin kamu yang asli!
    private static final String PASS = "1234"; 

    /**
     * Method statis untuk mengambil objek Connection.
     * Dapat dipanggil dari mana saja dengan: DatabaseConnection.getConnection()
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Memanggil Driver PostgreSQL (opsional di Java terbaru, tapi bagus untuk memastikan)
            Class.forName("org.postgresql.Driver");
            
            // Membuat koneksi
            conn = DriverManager.getConnection(URL, USER, PASS);
            // System.out.println("Koneksi Database Berhasil!"); // Un-comment jika ingin testing
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL tidak ditemukan! Cek pom.xml.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Gagal terhubung ke Database: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
```

```java
package com.upb.agripos.controller;

import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductFormView;
import javafx.scene.control.Alert;

public class ProductController {
    private final ProductService service;
    private final ProductFormView view;

    public ProductController(ProductFormView view) {
        this.view = view;
        this.service = new ProductService();
        
        // 1. Load data awal
        refreshData();
        
        // 2. Hubungkan Tombol dengan Method
        this.view.getBtnAdd().setOnAction(e -> tambahProduk());
    }

    private void tambahProduk() {
        try {
            String code = view.getTxtCode().getText();
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());

            service.addProduct(code, name, price, stock);

            refreshData();
            view.clearFields();
            showAlert("Sukses", "Produk berhasil ditambahkan!");

        } catch (NumberFormatException ex) {
            showAlert("Error", "Harga dan Stok harus angka!");
        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private void refreshData() {
        view.getListView().getItems().clear();
        try {
            service.getAllProducts().forEach(p -> 
                view.getListView().getItems().add(p.getCode() + " - " + p.getName() + " (Stok: " + p.getStock() + ")")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
```

```java
package com.upb.agripos.dao;

import java.util.List;
import com.upb.agripos.model.Product;

public interface ProductDAO {
    void insert(Product product) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;
    void update(Product product) throws Exception;
    void delete(String code) throws Exception;
}
```
```java
package com.upb.agripos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {
    private final Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) throws Exception {
        String sql = "INSERT INTO products(code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
            System.out.println("[DAO] Berhasil menyimpan: " + p.getName());
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY code";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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

    @Override
    public void update(Product p) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getCode());
            ps.executeUpdate();
            System.out.println("[DAO] Berhasil update: " + p.getName());
        }
    }

    @Override
    public void delete(String code) throws Exception {
        String sql = "DELETE FROM products WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
            System.out.println("[DAO] Berhasil menghapus: " + code);
        }
    }
}
```

```java
package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    // Setters (Untuk update)
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}
```

```java
package com.upb.agripos.service;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    private ProductDAO dao;

    public ProductService() {
        this.dao = new ProductDAOImpl(DatabaseConnection.getConnection());
    }

    public void addProduct(String code, String name, double price, int stock) throws Exception {
        if (price < 0) throw new Exception("Harga tidak boleh negatif!");
        dao.insert(new Product(code, name, price, stock));
    }

    public List<Product> getAllProducts() throws Exception {
        return dao.findAll();
    }
}
```

```java
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
```

```java
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
```

```java
package com.upb.agripos;

public class Launcher {
    public static void main(String[] args) {
        // Memanggil main method yang asli di AppJavaFX
        AppJavaFX.main(args);
    }
}
```

```java
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.upb</groupId>
    <artifactId>agripos-gui</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <javafx.version>21</javafx.version>
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
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.upb.agripos.AppJavaFX</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
---

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
![Screenshot hasil](/praktikum/week12-gui-dasar/screenshots/Screenshot%202026-01-23%20163610%20week%2012.png)
)
---

## Analisis

- Alur Jalannya Kode Aplikasi berjalan dengan alur MVC Strict. Saat pengguna mengklik tombol "Tambah", ProductController menangkap event tersebut. Controller kemudian mengambil data dari text field di View, mengirimnya ke ProductService untuk validasi, lalu Service memerintahkan ProductDAO untuk menyimpan data ke PostgreSQL. Setelah sukses, Controller meminta View untuk membersihkan form dan memperbarui ListView.  
- Perbedaan dengan Minggu Sebelumnya Pada Week 11, interaksi dilakukan melalui Unit Test atau Terminal (CLI). Pada Week 12 ini, interaksi sudah berbasis GUI (Visual) menggunakan JavaFX, sehingga lebih user-friendly. Selain itu, struktur kode lebih kompleks karena adanya pemisahan tanggung jawab antara tampilan (View) dan logika (Controller).
- Traceability (Kesesuaian dengan Bab 6)
   - Use Case Tambah Produk diimplementasikan pada tombol btnAdd.
   - Activity Diagram (Validasi -> Simpan) terjadi di dalam method addProduct pada Controller dan Service.
   - Sequence Diagram terealisasi dengan urutan panggil: View -> Controller -> Service -> DAO -> Database.
- Kendala & Solusi Sempat terjadi error JavaFX not found dan AppJavaFX cannot be resolved. Hal ini diatasi dengan memastikan pom.xml memuat versi JavaFX 21, melakukan Reload Project Maven, dan melakukan Clean Java Language Server Workspace di VS Code.

---

## Kesimpulan
Praktikum minggu ini berhasil mengimplementasikan aplikasi desktop berbasis JavaFX yang terintegrasi dengan database. Penggunaan arsitektur MVC terbukti membuat kode lebih terstruktur, di mana perubahan pada tampilan (View) tidak merusak logika bisnis (Service/DAO). Konsep Event-Driven Programming telah diterapkan dengan sukses pada fitur tombol "Tambah Produk".

---
## Tabel Traceability
### Tabel Traceability: Realisasi Desain Bab 6 ke Implementasi Week 12

Berikut adalah pemetaan antara desain sistem (UML) yang dirancang pada Bab 6 dengan kode program JavaFX yang dibuat pada Week 12:

| Artefak Desain (Bab 6) | Implementasi Kode (Week 12) | Lokasi File / Method | Keterangan |
| :--- | :--- | :--- | :--- |
| **Use Case:**<br>Input Data Produk | Form GUI (`GridPane`) dengan TextField Kode, Nama, Harga, Stok. | `view/ProductFormView.java`<br>(Method `setupUI`) | User memasukkan data melalui komponen GUI yang disediakan. |
| **Use Case:**<br>Klik Simpan | Event Handler pada Tombol Tambah (`btnAdd`). | `controller/ProductController.java`<br>(Line: `view.getBtnAdd().setOnAction(...)`) | Aksi klik tombol memicu eksekusi logika program. |
| **Activity Diagram:**<br>Validasi Input | Pengecekan tipe data (Parsing `Double`/`Integer`) dan try-catch block. | `controller/ProductController.java`<br>(Method `tambahProduk`) | Mencegah error jika user memasukkan huruf pada kolom harga/stok. |
| **Sequence Diagram:**<br>UI -> Controller -> Service | Pemanggilan method secara berjenjang: View memanggil Controller, Controller memanggil Service. | `ProductController` memanggil `service.addProduct(...)` | Penerapan arsitektur MVC Strict dan Layered Architecture. |
| **Sequence Diagram:**<br>Service -> DAO -> Database | Service memanggil DAO untuk eksekusi query `INSERT` SQL. | `dao/ProductDAOImpl.java`<br>(Method `insert`) | Data tersimpan permanen ke tabel PostgreSQL `products`. |
| **UI Design:**<br>Tampil Daftar Produk | Menggunakan komponen `ListView` yang di-refresh setelah data tersimpan. | `controller/ProductController.java`<br>(Method `refreshData`) | Tampilan diperbarui otomatis (*real-time*) setelah data masuk. |

---