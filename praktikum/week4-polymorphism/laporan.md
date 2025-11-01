# Laporan Praktikum Minggu 4
Topik: [Polymorphism]

## Identitas
- Nama  : [HANDIKA DWI ARDIYANTO]
- NIM   : [240202863]
- Kelas : [3IKRA]

---

## Tujuan
~Mahasiswa mampu menjelaskan konsep polymorphism dalam OOP.
~Mahasiswa mampu membedakan method overloading dan overriding.
~Mahasiswa mampu mengimplementasikan polymorphism dengan overloading, overriding, dan dynamic binding pada Java.
~Mahasiswa mampu menerapkan polymorphism pada kasus Agri-POS.

---

## Dasar Teori
1. Polymorphism berarti “banyak bentuk”, memungkinkan objek berbeda merespons method yang sama dengan cara berbeda.
2. Method Overloading terjadi ketika ada beberapa method dengan nama sama tetapi parameter berbeda.
3. Method Overriding adalah proses subclass mengganti method yang ada pada superclass dengan implementasi yang lebih spesifik.
4. Dynamic Binding menentukan method yang dipanggil saat runtime berdasarkan objek aktual, bukan tipe referensi.
5. Polymorphism meningkatkan fleksibilitas dan extensibility dalam desain sistem OOP.

---

## Langkah Praktikum
1. Menambahkan method tambahStok() (overloading) pada class Produk.
2. Membuat method getInfo() pada Produk dan di-override di subsystem: Benih, Pupuk, AlatPertanian.
3. Membuat array Produk[] berisi objek subclass.
4. Melakukan loop array dan memanggil getInfo() untuk melihat mekanisme dynamic binding.
5. Menjalankan program dan mengambil screenshot hasil.
6. Commit dengan pesan: week4-polymorphism

---

## Kode Program
AlatPertanian.java
```java
package main.java.com.upb.agripos.model;

public class AlatPertanian extends Produk {
    private String bahan;

    public AlatPertanian(String kode, String nama, double harga, int stok, String bahan) {
        super(kode, nama, harga, stok);
        this.bahan = bahan;
    }

    @Override
    public String getInfo() {
        return "Alat Pertanian: " + super.getInfo() + ", Bahan: " + bahan;
    }
}
```
Benih.java
```java
package main.java.com.upb.agripos.model;

public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    @Override
    public String getInfo() {
        return "Benih: " + super.getInfo() + ", Varietas: " + varietas;
    }
}
```
ObatHama.java
```java
package main.java.com.upb.agripos.model;

public class ObatHama extends Produk {
    private String kandungan;

    public ObatHama(String kode, String nama, double harga, int stok, String kandungan) {
        super(kode, nama, harga, stok);
        this.kandungan = kandungan;
    }

    @Override
    public String getInfo() {
        return "Obat Hama: " + super.getInfo() + ", Kandungan: " + kandungan;
    }
}
```
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

    // Overloading: dua versi tambahStok
    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public void tambahStok(double jumlah) {
        this.stok += (int) jumlah;
    }

    public String getInfo() {
        return "Produk: " + nama + " (Kode: " + kode + "), Harga: " + harga + ", Stok: " + stok;
    }

    // Getter opsional
    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
}
```
Pupuk.java
```java
package main.java.com.upb.agripos.model;

public class Pupuk extends Produk {
    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    @Override
    public String getInfo() {
        return "Pupuk: " + super.getInfo() + ", Jenis: " + jenis;
    }
}
```
CreditBy.java
```java
package main.java.com.upb.agripos.util;

public class CreditBy {
    public static void print(String nim, String nama) {
        System.out.println("\n=== Credit By ===");
        System.out.println("NIM  : " + nim);
        System.out.println("Nama : " + nama);
        System.out.println("=================");
    }
}
```
MainPolymorphism.java
```java
package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.util.CreditBy;

public class MainPolymorphism {
    public static void main(String[] args) {
        Produk[] daftarProduk = {
            new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64"),
            new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea"),
            new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja"),
            new ObatHama("OBH-701", "Anti Wereng", 120000, 25, "Karbofuran")
        };

        System.out.println("=== Info Produk (Dynamic Binding) ===");
        for (Produk p : daftarProduk) {
            System.out.println(p.getInfo()); // dynamic binding: Java panggil method sesuai tipe objek
        }

        // Contoh overloading
        System.out.println("\n=== Contoh Overloading ===");
        Produk contoh = new Produk("TES-001", "Produk Tes", 10000, 10);
        contoh.tambahStok(5);      // tambah 5 stok
        contoh.tambahStok(3.5);    // tambah 3 stok (double)
        System.out.println(contoh.getInfo());

        CreditBy.print("<240202863>", "<HANDIKA DWI ARDIYANTO>");
    }
}
```

---

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
![Screenshot hasil](/praktikum/week4-polymorphism/screenshots/Screenshot%202025-11-01%20110332%20week%204.png)
)
---

## Analisis
~Program menunjukkan overloading pada tambahStok() dan overriding pada getInfo() di tiap subclass.
~Ketika objek disimpan dalam array Produk[], pemanggilan getInfo() tetap mengikuti tipe objek sebenarnya (dynamic binding).
~Dibanding minggu sebelumnya, konsep OOP lebih kompleks dan fokus pada perilaku objek, bukan sekadar struktur class.
~Kendala: memastikan method override benar & struktur package sesuai.
~Solusi: menggunakan anotasi @Override dan pengecekan structure folder/namespace.
---

## Kesimpulan
Polymorphism memberikan fleksibilitas pada program dengan memungkinkan objek yang berbeda merespons method yang sama secara berbeda. Dengan overloading, overriding, dan dynamic binding, sistem menjadi modular dan mudah dikembangkan—seperti implementasi Agri-POS pada produk pertanian.

---

## Quiz
Apa perbedaan overloading dan overriding?
Jawaban:
Overloading: method sama, parameter berbeda dalam satu class.
Overriding: method di subclass mengganti method di superclass.

Bagaimana Java menentukan method mana yang dipanggil dalam dynamic binding?
Jawaban:
Ditentukan saat runtime berdasarkan objek sebenarnya, bukan tipe referensi variabel.

Berikan contoh kasus polymorphism dalam sistem POS selain produk pertanian.
Jawaban:
Class Pembayaran dengan subclass Tunai, E-Wallet, KartuDebit, masing-masing override prosesPembayaran().