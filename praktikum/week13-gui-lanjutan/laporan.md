# Laporan Praktikum Minggu 13
Topik: GUI Lanjutan JavaFX (TableView dan Lambda Expression)

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
1. Mahasiswa mampu menampilkan data kompleks (baris & kolom) menggunakan komponen TableView JavaFX.
2. Mahasiswa mampu mengintegrasikan koleksi objek (List) dari database ke dalam GUI.
3. Mahasiswa mampu menggunakan Lambda Expression untuk membuat event handler yang lebih ringkas.
4. Mahasiswa mampu menghubungkan GUI dengan DAO secara penuh, termasuk fitur hapus data (Delete) yang terintegrasi ke database.
---

## Dasar Teori
- TableView: Komponen JavaFX yang digunakan untuk memvisualisasikan data dalam bentuk tabel. Berbeda dengan ListView yang hanya menampilkan satu item per baris, TableView membagi data menjadi kolom-kolom (misal: kolom Harga, kolom Stok) yang memudahkan pembacaan data terstruktur.
- Lambda Expression: Fitur Java (sejak Java 8) yang menyederhanakan penulisan implementasi functional interface. Dalam JavaFX, ini sangat berguna untuk menangani event tombol, mengubah kode new EventHandler... yang panjang menjadi sintaks ringkas e -> { ... }.
- Data Binding (PropertyValueFactory): Mekanisme untuk menghubungkan kolom tabel dengan atribut di dalam class Model. Misalnya, kolom "Harga" secara otomatis mengambil nilai dari method getPrice() tanpa perlu penulisan kode manual.

---

## Langkah Praktikum

1. Refactoring View: Mengubah class ProductFormView menjadi ProductTableView. Mengganti komponen ListView dengan TableView dan mendefinisikan kolom-kolomnya.  
2. Update DAO & Service: Menambahkan method delete(String code) pada ProductDAOImpl dan ProductService agar aplikasi bisa menghapus data di database PostgreSQL.
3. Update Controller: Mengubah logika controller untuk mendukung TableView.
4. Implementasi Lambda: Menggunakan Lambda Expression pada event tombol Tambah dan Hapus.
5. Running & Testing: Menjalankan aplikasi, memastikan data muncul di tabel, dan mencoba fitur hapus data.

---

## Kode Program

ProductController.java
```java
package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductTableView; // <-- IMPORT YANG BARU
import javafx.scene.control.Alert;

public class ProductController {
    private final ProductService service;
    private final ProductTableView view; // <-- GANTI TIPE DATA INI

    // GANTI PARAMETER DI CONSTRUCTOR
    public ProductController(ProductTableView view) {
        this.view = view;
        this.service = new ProductService();
        
        refreshTable();

        this.view.getBtnAdd().setOnAction(e -> handleAddProduct());
        this.view.getBtnDelete().setOnAction(e -> handleDeleteProduct());
    }

    // ... (SISA KODE KE BAWAH SAMA PERSIS SEPERTI SEBELUMNYA) ...
    
    private void handleAddProduct() {
        try {
            String code = view.getTxtCode().getText();
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());

            service.addProduct(code, name, price, stock);
            refreshTable();
            view.clearFields();
            showAlert("Sukses", "Data berhasil disimpan!");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void handleDeleteProduct() {
        Product selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.deleteProduct(selected.getCode());
                refreshTable();
                showAlert("Sukses", "Data dihapus!");
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        } else {
            showAlert("Warning", "Pilih data dulu!");
        }
    }

    private void refreshTable() {
        view.getTable().getItems().clear();
        view.getTable().getItems().addAll(service.getAllProducts());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
```

ProductDAO.java
```java
package com.upb.agripos.dao;
import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    void insert(Product p) throws Exception;
    void delete(String code) throws Exception; // <-- TAMBAHAN WEEK 13
    List<Product> findAll() throws Exception;
}
```

ProductDAOImpl.java
```java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private Connection conn;

    // 1. Definisikan Info Database langsung di sini
    private final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private final String USER = "postgres";
    private final String PASS = "your_password"; // <--- JANGAN LUPA GANTI PASSWORD INI

    public ProductDAOImpl() {
        // 2. Buat koneksi langsung di Constructor tanpa memanggil file lain
        try {
            // Memastikan Driver PostgreSQL terbaca
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Gagal koneksi database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Product p) throws Exception {
        // Pastikan koneksi tidak null sebelum dipakai
        if (conn == null) throw new Exception("Koneksi Database Belum Terbuka!");

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
        if (conn == null) throw new Exception("Koneksi Database Belum Terbuka!");

        String sql = "DELETE FROM products WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        if (conn == null) throw new Exception("Koneksi Database Belum Terbuka!");

        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY code";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"), rs.getString("name"),
                    rs.getDouble("price"), rs.getInt("stock")
                ));
            }
        }
        return list;
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

    // Constructor Kosong (Opsional tapi disarankan ada)
    public Product() {
    }

    // Constructor Lengkap
    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // --- GETTER METHODS (PENTING UNTUK TABLEVIEW) ---
    // Tanpa Getter ini, tabel akan KOSONG/BLANK.
    // Nama getter harus sesuai pola: get + NamaVariabel (Huruf depan besar)
    
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

    // --- SETTER METHODS ---
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

ProductService.java
```java
package com.upb.agripos.service;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    private ProductDAO dao;

    public ProductService() {
        this.dao = new ProductDAOImpl();
    }

    public void addProduct(String code, String name, double price, int stock) throws Exception {
        if (price < 0) throw new Exception("Harga tidak boleh negatif!");
        if (stock < 0) throw new Exception("Stok tidak boleh negatif!");
        dao.insert(new Product(code, name, price, stock));
    }

    // --- TAMBAHAN WEEK 13 ---
    public void deleteProduct(String code) throws Exception {
        dao.delete(code);
    }

    public List<Product> getAllProducts() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
```

ProductTableView.java
```java
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
```

AppJavaFX.java
```java
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

Pom.xml
```java
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.upb</groupId>
    <artifactId>agripos-gui-lanjutan</artifactId>
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
        </plugins>
    </build>
</project>
```

---

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
![Screenshot hasil](/praktikum/week13-gui-lanjutan/screenshots/Screenshot%202026-01-23%20183132%20week%2013.png)
)
---

## Analisis

- Analisis Kode Kode berjalan dengan pola MVC yang ketat. TableView diinisialisasi di View, namun pengisian datanya (populate) dilakukan oleh Controller melalui method refreshTable(). Penggunaan Lambda Expression pada setOnAction membuat kode Controller jauh lebih bersih dibandingkan Anonymous Inner Class. Saat tombol hapus diklik, aplikasi tidak menghapus baris tabel secara langsung, melainkan menghapus data di database terlebih dahulu, baru kemudian memuat ulang (reload) seluruh tabel untuk memastikan sinkronisasi data.
- Perbedaan Pendekatan (vs Minggu Lalu) Minggu ini kita beralih dari ListView ke TableView. ListView hanya cocok untuk data sederhana (string tunggal), sedangkan TableView sangat esensial untuk aplikasi bisnis (POS) karena mampu menampilkan detail atribut (Harga, Stok) secara terpisah dan terurut.
- Tabel Traceability (Bab 6 -> Bab 13) Sesuai instruksi modul, berikut pemetaan desain UML ke implementasi kode:
### Tabel Traceability: Realisasi Desain Bab 6 ke Implementasi Week 13

Berikut adalah pemetaan antara desain sistem (UML) yang dirancang pada Bab 6 dengan kode program JavaFX yang dibuat pada Week 13:

| Artefak Bab 6 | Referensi | Handler GUI (Week 13) | Controller/Service | DAO | Dampak UI/DB |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Use Case** | UC-02 Lihat Daftar Produk | Method `refreshTable()` dipanggil saat inisialisasi Controller | `ProductController` memanggil `ProductService.getAllProducts()` | `ProductDAO.findAll()` (Query: `SELECT *`) | `TableView` otomatis terisi data terbaru dari Database PostgreSQL saat aplikasi dibuka. |
| **Use Case** | UC-03 Hapus Produk | Event Handler pada Tombol Hapus (`btnDelete`) menggunakan Lambda | `ProductController` memanggil `ProductService.deleteProduct(code)` | `ProductDAO.delete(code)` (Query: `DELETE`) | Data terhapus permanen dari Database dan baris tabel di UI hilang seketika. |
| **Sequence Diagram** | SD-02 Hapus Produk | Event `setOnAction` (Lambda Expression) | Alur: **View** (Klik) &rarr; **Controller** (Handle) &rarr; **Service** (Validasi) | Alur: **Service** &rarr; **DAO** (Eksekusi SQL) &rarr; **Database** | Implementasi kode mengikuti urutan Sequence Diagram (Layered Architecture) secara ketat. |
| **Class Diagram** | Relasi View-Model | Komponen `TableView<Product>` | `ProductTableView` menggunakan Class Model `Product` | - | Kolom tabel (`TableColumn`) terikat langsung dengan atribut Model (Kode, Nama, Harga, Stok). |
---

## Kesimpulan
Praktikum minggu ini berhasil mengimplementasikan fitur GUI lanjutan yang krusial untuk aplikasi Agri-POS. Penggunaan TableView membuat penyajian data menjadi informatif, dan penerapan Lambda Expression meningkatkan efisiensi penulisan kode. Integrasi fitur Delete melengkapi operasi CRUD dasar yang menghubungkan UI hingga ke Database PostgreSQL dengan aman melalui lapisan Service.

---