# Laporan Praktikum Minggu 2
Topik: Class dan Object

## Identitas
- Nama  : [HANDIKA DWI ARDIYANTO]
- NIM   : [240202863]
- Kelas : [3IKRA]

---

## Tujuan
Mahasiswa memahami konsep class dan object, serta mampu mengimplementasikan enkapsulasi dalam pembuatan class Produk dan pemanggilannya di class MainProduk.

---

## Dasar Teori
1. Class adalah blueprint atau rancangan untuk membuat objek.
2. Object adalah instance dari class yang memiliki atribut dan perilaku.
3. Enkapsulasi adalah konsep OOP untuk melindungi data dengan menjadikannya private dan mengaksesnya menggunakan getter dan setter.
4. Method adalah fungsi yang berada di dalam class dan digunakan untuk memproses data atau melakukan operasi tertentu.
5. Package digunakan untuk mengelompokkan class agar struktur program lebih terorganisir.

---

## Langkah Praktikum
1. Membuat package:
   -main.java.com.upb.agripos.model untuk class Produk.
   -main.java.com.upb.agripos.util untuk class CreditBy.
   -main.java.com.upb.agripos untuk class MainProduk.
2. Membuat class Produk yang memiliki atribut kode, nama, harga, dan stok, serta method untuk menambah dan mengurangi stok.
3. Membuat class CreditBy yang menampilkan identitas mahasiswa pembuat program.
4. Membuat class MainProduk untuk menjalankan program utama, menampilkan data produk, dan melakukan simulasi perubahan stok.
5. Menjalankan program dan melihat hasil eksekusi di console.
6. Commit kode ke repository dengan pesan:
"Menambahkan class Produk, CreditBy, dan MainProduk"

---

## Kode Program  
Produk.java
```java
package main.java.com.upb.agripos.model;

public class Produk {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public void kurangiStok(int jumlah) {
        if (this.stok >= jumlah) {
            this.stok -= jumlah;
        } else {
            System.out.println("Stok tidak mencukupi!");
        }
    }
}
```
CreditBy.java
```java
package main.java.com.upb.agripos.util;

public class CreditBy {
    public static void print(String nim, String nama) {
        System.out.println("\ncredit by: " + nim + " - " + nama);
    }
}
```
MainProduk
```java
package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.Produk;
import main.java.com.upb.agripos.util.CreditBy;

public class MainProduk {
    public static void main(String[] args) {
        Produk p1 = new Produk("BNH-001", "Benih Padi IR64", 25000, 100);
        Produk p2 = new Produk("PPK-101", "Pupuk Urea 50kg", 350000, 40);
        Produk p3 = new Produk("ALT-501", "Cangkul Baja", 90000, 15);

        System.out.println("Kode: " + p1.getKode() + ", Nama: " + p1.getNama() + ", Harga: " + p1.getHarga() + ", Stok: " + p1.getStok());
        System.out.println("Kode: " + p2.getKode() + ", Nama: " + p2.getNama() + ", Harga: " + p2.getHarga() + ", Stok: " + p2.getStok());
        System.out.println("Kode: " + p3.getKode() + ", Nama: " + p3.getNama() + ", Harga: " + p3.getHarga() + ", Stok: " + p3.getStok());

        System.out.println("\nMenambah stok Benih Padi IR64 sebanyak 20...");
        p1.tambahStok(20);
        System.out.println("Stok baru: " + p1.getStok());

        System.out.println("Mengurangi stok Pupuk Urea sebanyak 5...");
        p2.kurangiStok(5);
        System.out.println("Stok baru: " + p2.getStok());

        CreditBy.print("240202863", "HANDIKA DWI ARDIYANTO");
    }
}
---
```
## Hasil Eksekusi
![Screenshot hasil](/praktikum/week2-class-object/screenshots/Screenshot%202025-10-09%20210230.png)
---
```
```
## Analisis
-Program ini terdiri dari tiga class: Produk, CreditBy, dan MainProduk.
-Class Produk menerapkan enkapsulasi, di mana atribut bersifat private dan diakses melalui getter dan setter.
-Method tambahStok() dan kurangiStok() menunjukkan contoh perilaku (behavior) dari objek Produk.
-Class CreditBy menampilkan identitas mahasiswa, menegaskan pentingnya utility class yang berfungsi untuk mencetak data tambahan.
-Class MainProduk menjadi driver class untuk menjalankan seluruh logika.
-Tidak ada error saat program dijalankan.
-Kendala kecil: perlu memastikan struktur package sesuai agar import berjalan lancar di IDE seperti IntelliJ atau Eclipse.
)**
---

---
```
```
## Kesimpulan
Dengan mempelajari konsep class dan object, mahasiswa dapat memahami bagaimana data dan fungsi dapat dikemas dalam satu kesatuan.
Penerapan enkapsulasi membuat data lebih aman dan pengelolaan stok menjadi lebih terstruktur serta mudah dikembangkan.
```
---
```
## Quiz
1. Mengapa atribut sebaiknya dideklarasikan sebagai private dalam class?
Jawaban:
Agar data tidak dapat diakses atau diubah secara langsung dari luar class, melainkan harus melalui method khusus seperti getter dan setter.
Hal ini menjaga keamanan dan konsistensi data dalam objek.

2. Apa fungsi getter dan setter dalam enkapsulasi?
Jawaban:
Getter digunakan untuk mengambil nilai atribut, sedangkan Setter digunakan untuk mengubah nilai atribut secara terkontrol.
Dengan cara ini, data hanya dapat dimodifikasi sesuai logika yang telah ditentukan di dalam class.

3. Bagaimana cara class Produk mendukung pengembangan aplikasi POS yang lebih kompleks?
Jawaban:
Class Produk dapat menjadi dasar dari sistem POS (Point of Sale) dengan menambahkan fitur tambahan seperti kategori produk, harga diskon, laporan penjualan, atau manajemen inventori.
Struktur class yang modular dan terenkapsulasi memudahkan pengembangan lebih lanjut tanpa harus mengubah struktur utama program.
<<<<<<< HEAD
=======
```
>>>>>>> d51bb77 (upload tugas)
