# Laporan Praktikum Minggu 11
Topik: Data Access Object (DAO) dan CRUD Database dengan JDBC (PostgreSQL)

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
- Mahasiswa mampu menjelaskan konsep Data Access Object (DAO) sebagai pola desain untuk memisahkan logika bisnis dari logika akses data.
- Mahasiswa mampu menghubungkan aplikasi Java dengan basis data PostgreSQL menggunakan JDBC.
- Mahasiswa mampu mengimplementasikan operasi CRUD (Create, Read, Update, Delete) secara lengkap menggunakan PreparedStatement.
- Mahasiswa mampu mengintegrasikan DAO ke dalam aplikasi utama agar kode menjadi lebih rapi dan modular.

---

## Dasar Teori
- DAO (Data Access Object): Design pattern yang menyediakan antarmuka abstrak ke database. Tujuannya adalah memisahkan logika aplikasi dari detail teknis penyimpanan data, sehingga perubahan pada database tidak merusak logika bisnis.
- JDBC (Java Database Connectivity): Standar API Java untuk menghubungkan aplikasi dengan database relasional.
- PostgreSQL JDBC Driver: Komponen software (library) yang memungkinkan aplikasi Java berkomunikasi dengan server database PostgreSQL.
- PreparedStatement: Objek JDBC yang digunakan untuk mengeksekusi query SQL yang sudah dikompilasi sebelumnya. Ini lebih aman dari serangan SQL Injection dibandingkan Statement biasa.

---

## Langkah Praktikum
- Menyiapkan database agripos dan tabel products menggunakan pgAdmin 4 (PostgreSQL) atau Query Tool.
- Mengkonfigurasi file pom.xml untuk menambahkan dependency driver PostgreSQL (org.postgresql:postgresql) agar proyek mengenali database tersebut.
- Membuat class Model Product yang merepresentasikan data tabel.
- Membuat Interface ProductDAO yang mendefinisikan kontrak operasi CRUD (Insert, Find, Update, Delete).
- Membuat class implementasi ProductDAOImpl yang berisi kode JDBC untuk mengeksekusi query SQL ke PostgreSQL.
- Membuat class MainDAOTest yang melakukan koneksi ke database dan menguji seluruh operasi CRUD secara berurutan.
- Melakukan commit ke repository dengan pesan: week11-dao: implementasi dao pattern dengan postgresql.

---

## Kode Program
ProductDAO.java
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
ProductDAOImpl.java
```java
package main.java.com.upb.agripos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.com.upb.agripos.model.Product;

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
Product.java
```java
package main.java.com.upb.agripos.model;

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
MainDAOTest.java
```java
package main.java.com.upb.agripos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import main.java.com.upb.agripos.dao.ProductDAO;
import main.java.com.upb.agripos.dao.ProductDAOImpl;
import main.java.com.upb.agripos.model.Product;

public class MainDAOTest {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week11 - PostgreSQL)");
        
        // --- KONFIGURASI POSTGRESQL ---
        String url = "jdbc:postgresql://localhost:5432/agripos";
        String user = "postgres";     // Default user PostgreSQL
        String password = "1234"; // GANTI DENGAN PASSWORD ANDA SAAT INSTAL POSTGRES
        // ------------------------------

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            ProductDAO dao = new ProductDAOImpl(conn);

            // 1. Insert
            System.out.println("\n--- 1. INSERT DATA ---");
            Product pNew = new Product("PG-01", "Bibit Durian Musang King", 150000, 10);
            dao.insert(pNew);

            // 2. Read
            System.out.println("\n--- 2. READ ALL DATA ---");
            List<Product> all = dao.findAll();
            for (Product p : all) {
                System.out.println("- " + p.getCode() + " | " + p.getName() + " | Rp" + p.getPrice());
            }

            // 3. Update
            System.out.println("\n--- 3. UPDATE DATA ---");
            Product pEdit = dao.findByCode("PG-01");
            if (pEdit != null) {
                pEdit.setStock(5); // Stok berkurang
                dao.update(pEdit);
            }

            // 4. Delete
            System.out.println("\n--- 4. DELETE DATA ---");
            dao.delete("PG-01");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal koneksi! Cek url, user, password PostgreSQL Anda.");
        }
    }
}
```

---

## Hasil Eksekusi

![Screenshot hasil](/praktikum/week11-dao-database/screenshots/crud_result.png)

---

## Analisis
---
- Koneksi Database: Aplikasi berhasil terhubung ke PostgreSQL port 5432. Penggunaan driver org.postgresql di pom.xml sangat krusial agar Java mengenali protokol database ini.
- Pemisahan Logika (DAO): Logika SQL tersimpan rapi di dalam ProductDAOImpl. Class utama (MainDAOTest) menjadi sangat bersih karena tidak ada kode SQL (INSERT INTO...) yang tercampur di sana. Main program hanya memanggil method seperti dao.insert() atau dao.findAll().
- Keamanan Data: Penggunaan PreparedStatement (tanda tanya ?) pada kode DAO melindungi aplikasi dari error sintaks akibat karakter khusus (misal tanda kutip pada nama produk) dan mencegah SQL Injection.
- Fleksibilitas: Jika suatu saat database ingin diganti kembali ke MySQL, kita cukup mengubah driver di pom.xml dan URL koneksi di Main, tanpa perlu merombak logika bisnis aplikasi secara keseluruhan.
---

## Kesimpulan
Penerapan DAO Pattern dengan database PostgreSQL memberikan arsitektur aplikasi yang kuat (robust). Data tersimpan permanen di database server yang handal, dan kode program tetap bersih, aman, serta mudah dikembangkan (maintainable) karena adanya pemisahan tanggung jawab yang jelas antara logika bisnis dan akses data.

---

## Quiz
1. Apa fungsi pom.xml dalam praktikum ini?
   **Jawaban:** Untuk mengatur konfigurasi proyek Maven, khususnya mengunduh library PostgreSQL JDBC Driver secara otomatis dari repositori pusat, sehingga kita tidak perlu mencari dan menambahkan file JAR secara manual.

2. Mengapa kita perlu menutup koneksi (conn.close() atau block try-with-resources)?  
   **Jawaban:** Untuk membebaskan sumber daya (resource) di server database. Koneksi yang dibiarkan terbuka terus-menerus dapat menumpuk dan menyebabkan server database kehabisan slot koneksi (connection leak), yang akhirnya membuat aplikasi tidak bisa diakses.

3. Apa perbedaan JDBC URL untuk MySQL dan PostgreSQL?
   **Jawaban:** 
   - MySQL: jdbc:mysql://localhost:3306/nama_db
   - PostgreSQL: jdbc:postgresql://localhost:5432/nama_db (Perbedaan utama ada pada protokol sub-name postgresql vs mysql dan port default 5432 vs 3306).