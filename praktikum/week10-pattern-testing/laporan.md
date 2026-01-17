# Laporan Praktikum Minggu 10
Topik: Design Pattern (Singleton, MVC), Unit Testing, dan Manajemen Proyek Maven
## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
1. Mahasiswa mampu menjelaskan konsep dasar design pattern dalam rekayasa perangkat lunak.
2. Mahasiswa mampu mengimplementasikan Singleton Pattern untuk membatasi instansiasi objek database.
3. Mahasiswa mampu menerapkan arsitektur MVC (Model-View-Controller) untuk memisahkan logika aplikasi.
4. Mahasiswa mampu mengkonfigurasi Maven (pom.xml) untuk manajemen dependensi.
5. Mahasiswa mampu membuat dan menjalankan unit testing menggunakan JUnit 5.

---

## Dasar Teori
1. Design Pattern: Solusi standar untuk masalah umum dalam desain perangkat lunak yang dapat digunakan kembali.
2. Singleton Pattern: Pola desain yang memastikan sebuah class hanya memiliki satu instance dan menyediakan satu titik akses global ke instance tersebut.
3. MVC (Model-View-Controller): Pola arsitektur yang memisahkan aplikasi menjadi tiga komponen utama: Model (Data), View (Tampilan), dan Controller (Logika Penghubung).
4. Maven: Alat manajemen proyek Java yang menggunakan file pom.xml untuk mengatur dependensi (library) dan konfigurasi build secara otomatis.
5. Unit Testing (JUnit): Metode pengujian kode pada level komponen terkecil (unit/method) untuk memastikan fungsi berjalan sesuai spesifikasi dan bebas bug.

---

## Langkah Praktikum
1. Membuat proyek Maven baru dan mengkonfigurasi file pom.xml untuk menambahkan dependensi JUnit Jupiter (v5.10.0) dan mengatur versi Java ke 21.
2. Membuat struktur package yang rapi: config, controller, model, dan view.
3. Mengimplementasikan Singleton pada class DatabaseConnection agar koneksi database hanya dibuat satu kali.
4. Mengimplementasikan MVC pada fitur Produk yang terdiri dari class Product, ProductController, dan ConsoleView.
5. Membuat class AppMVC untuk menguji alur Singleton dan integrasi MVC.
6. Membuat unit test ProductTest untuk menguji validitas data model menggunakan assertion JUnit.
7. Melakukan commit ke repository dengan pesan: week10-pattern-testing: implementasi singleton, mvc, dan junit.

---

## Kode Program  
DatabaseConnection.java
```java
package main.java.com.upb.agripos.config;

public class DatabaseConnection {
    // 1. Static instance variable
    private static DatabaseConnection instance;

    // 2. Private constructor (agar tidak bisa di-new dari luar)
    private DatabaseConnection() {
        System.out.println("Database Connection Created (Hanya muncul sekali)");
    }

    // 3. Static method untuk akses global
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void connect() {
        System.out.println("Terhubung ke Database Agri-POS.");
    }
}
```
ProductController.java
```java
package main.java.com.upb.agripos.controller;

import main.java.com.upb.agripos.model.Product;
import main.java.com.upb.agripos.view.ConsoleView;

public class ProductController {
    private final Product model;
    private final ConsoleView view;

    public ProductController(Product model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    public void showProduct() {
        // Controller mengambil data dari Model
        String detail = "Produk: " + model.getCode() + " - " + model.getName();
        // Controller mengirim data ke View untuk ditampilkan
        view.showMessage(detail);
    }
}
```
Product.java
```java
package main.java.com.upb.agripos.model;

public class Product {
    private final String code;
    private final String name;

    public Product(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
}
```
ConsoleView.java
```java
package main.java.com.upb.agripos.view;

public class ConsoleView {
    public void showMessage(String message) {
        System.out.println("[VIEW] " + message);
    }
}
```
AppMVC.java
```java
package main.java.com.upb.agripos;

import main.java.com.upb.agripos.config.DatabaseConnection;
import main.java.com.upb.agripos.controller.ProductController;
import main.java.com.upb.agripos.model.Product;
import main.java.com.upb.agripos.view.ConsoleView;

public class AppMVC {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week10)");
        System.out.println("====================================================");

        // 1. Tes Singleton
        System.out.println("\n--- TEST SINGLETON ---");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        db1.connect();
        
        DatabaseConnection db2 = DatabaseConnection.getInstance(); // Tidak akan memanggil constructor lagi
        System.out.println("Apakah db1 sama dengan db2? " + (db1 == db2));

        // 2. Tes MVC
        System.out.println("\n--- TEST MVC PATTERN ---");
        Product model = new Product("P01", "Pupuk Organik");
        ConsoleView view = new ConsoleView();
        ProductController controller = new ProductController(model, view);

        controller.showProduct();
    }
}
```
ProductTest.java
```java
package test.java.com.upb.agripos;

import main.java.com.upb.agripos.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        // Arrange (Siapkan data)
        String kode = "P-99";
        String nama = "Bibit Unggul";

        // Act (Jalankan kode yang mau dites)
        Product p = new Product(kode, nama);

        // Assert (Pastikan hasilnya sesuai harapan)
        assertEquals(kode, p.getCode(), "Kode produk harus sesuai");
        assertEquals(nama, p.getName(), "Nama produk harus sesuai");
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
    <artifactId>agripos-app</artifactId>
    <version>1.0-SNAPSHOT</version>

<properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.10.0</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.10.0</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>src</sourceDirectory>
        <testSourceDirectory>src</testSourceDirectory>
    </build>
</project>
```

---

## Hasil Eksekusi
ProductTest.java  
![Screenshot hasil](/praktikum/week10-pattern-testing/screenshots/junit_result.png)

AppMVC.java
![Screenshot hasil](/praktikum/week10-pattern-testing/screenshots/Screenshot%202026-01-17%20161540%20week%2010%201.png)

---

## Analisis
---
- Maven (pom.xml): Penggunaan Maven sangat memudahkan manajemen library. Kita tidak perlu mengunduh file .jar JUnit secara manual; Maven secara otomatis mengunduhnya dari repositori pusat berdasarkan konfigurasi di pom.xml.
- Singleton: Implementasi Singleton pada DatabaseConnection berhasil mencegah pembuatan objek ganda. Hal ini diverifikasi di AppMVC di mana pemanggilan getInstance() berulang kali tetap mengembalikan objek referensi yang sama.
- MVC: Pemisahan kode menjadi Model (Data), View (Output), dan Controller (Logika) membuat struktur kode lebih rapi. Perubahan pada tampilan (ConsoleView) tidak akan merusak logika data (Product), sehingga kode lebih mudah dipelihara (maintainable).
- Unit Testing: JUnit test berhasil memverifikasi bahwa objek Product menyimpan data dengan benar. Jika test lolos (hijau), berarti logika dasar kode aman. Ini membantu mendeteksi bug lebih awal.

---

## Kesimpulan
Praktikum minggu ini memberikan pemahaman mendalam tentang standar pengembangan perangkat lunak modern. Penggunaan Maven mempermudah manajemen proyek, Design Pattern (Singleton & MVC) membuat arsitektur aplikasi lebih rapi dan efisien, serta Unit Testing menjamin kualitas kode agar bebas dari bug dasar sebelum aplikasi berkembang lebih kompleks.

---

## Quiz
1. Mengapa constructor pada Singleton harus bersifat private?
   **Jawaban:** Untuk mencegah class lain membuat instance baru secara sembarangan menggunakan keyword new. Akses instansiasi ditutup total dari luar, sehingga satu-satunya cara mendapatkan objek adalah melalui method static getInstance().

2. Jelaskan manfaat pemisahan Model, View, dan Controller.
   **Jawaban:** Memudahkan pemeliharaan kode (maintainability), memungkinkan pengembangan paralel (tim UI mengerjakan View, tim Backend mengerjakan Model), dan meningkatkan penggunaan kembali kode (reusability) karena komponen tidak saling terikat erat.

3. Apa peran unit testing dalam menjaga kualitas perangkat lunak?
   **Jawaban:** Mendeteksi kesalahan logika pada tahap awal pengembangan (early detection), mencegah regression bugs (bug lama muncul kembali saat ada perubahan baru), dan berfungsi sebagai dokumentasi teknis hidup tentang perilaku kode yang diharapkan.

4. Apa risiko jika Singleton tidak diimplementasikan dengan benar?
   **Jawaban:** Pada lingkungan multithreading (banyak proses berjalan bersamaan), jika Singleton tidak aman (not thread-safe), dua thread bisa masuk ke pengecekan if (instance == null) secara bersamaan dan menciptakan dua objek berbeda. Hal ini melanggar prinsip Singleton dan bisa menyebabkan inkonsistensi data.