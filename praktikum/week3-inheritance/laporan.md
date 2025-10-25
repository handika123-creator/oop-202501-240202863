# Laporan Praktikum Minggu 3
Topik: [Inheritance (Kategori Produk)]

## Identitas
- Nama  : [HANDIKA DWI ARDIYANTO]
- NIM   : [240202863]
- Kelas : [3IKRA]

---

## Tujuan
Mahasiswa mampu menjelaskan konsep inheritance (pewarisan class) dalam OOP.

Mahasiswa mampu membuat superclass dan subclass untuk produk pertanian.

Mahasiswa mampu mendemonstrasikan hierarki class melalui contoh kode.

Mahasiswa mampu menggunakan super untuk memanggil konstruktor dan method parent class.

Mahasiswa mampu membuat laporan praktikum yang menjelaskan perbedaan penggunaan inheritance dibanding class tunggal.*

---

## Dasar Teori
Inheritance (pewarisan) adalah mekanisme dalam OOP yang memungkinkan sebuah class mewarisi atribut dan method dari class lain.

Superclass (induk) adalah class yang memberikan atribut dan perilaku umum kepada subclass.

Subclass (turunan) adalah class yang mewarisi semua anggota dari superclass, tetapi dapat menambahkan atribut/method baru.

Keyword super digunakan untuk memanggil konstruktor atau method milik superclass.

Inheritance membantu mengurangi duplikasi kode dan membuat sistem lebih mudah diperluas (reusable & maintainable).

---

## Langkah Praktikum
Gunakan class Produk dari minggu sebelumnya sebagai superclass.

Buat tiga subclass:

Benih.java → atribut tambahan: varietas

Pupuk.java → atribut tambahan: jenis

AlatPertanian.java → atribut tambahan: material

Buat file MainInheritance.java untuk menginstansiasi setiap subclass.

Tambahkan class CreditBy.java untuk menampilkan identitas mahasiswa.

Jalankan program dan pastikan semua objek ditampilkan dengan benar.

---

## Kode Program
AlatPertanian
```java
package main.java.com.upb.agripos.model;

public class AlatPertanian extends Produk {
    private String material;

    public AlatPertanian(String kode, String nama, double harga, int stok, String material) {
        // Memanggil konstruktor superclass (Produk)
        super(kode, nama, harga, stok);
        this.material = material;
    }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    // Override method deskripsi untuk menambahkan info material
    @Override
    public String deskripsi() {
        return super.deskripsi() + ", Material: " + this.material;
    }
}
```
Benih.java
```java
package main.java.com.upb.agripos.model;
    public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        // Memanggil konstruktor superclass (Produk)
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    public String getVarietas() { return varietas; }
    public void setVarietas(String varietas) { this.varietas = varietas; }

    // Override method deskripsi untuk menambahkan info varietas
    @Override
    public String deskripsi() {
        return super.deskripsi() + ", Varietas: " + this.varietas;
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

    // Getters
    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }

    // Setters
    public void setKode(String kode) { this.kode = kode; }
    public void setNama(String nama) { this.nama = nama; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = stok; }

    /**
     * Method untuk menampilkan deskripsi dasar produk.
     * Method ini akan di-override oleh subclass untuk informasi yang lebih spesifik.
     */
    public String deskripsi() {
        return "Kode: " + this.kode +
               ", Nama: " + this.nama +
               ", Harga: Rp" + String.format("%,.2f", this.harga) +
               ", Stok: " + this.stok;
    }
}
```
Pupuk.java
```java
package main.java.com.upb.agripos.model;

public class Pupuk extends Produk {
    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        // Memanggil konstruktor superclass (Produk)
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    // Override method deskripsi untuk menambahkan info jenis
    @Override
    public String deskripsi() {
        return super.deskripsi() + ", Jenis: " + this.jenis;
    }
}
```
CreaditBy.java
```java
public class CreditBy {
    public static void print(String nim, String nama) {
        System.out.println("\n=================================");
        System.out.println("Tugas ini dikerjakan oleh:");
        System.out.println("NIM  : " + nim);
        System.out.println("Nama : " + nama);
        System.out.println("=================================");
    }
}
```
MainInheritance.java
```java
package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.util.CreditBy;

public class MainInheritance {
    public static void main(String[] args) {
        // Instansiasi objek dari tiap subclass
        Benih benihPadi = new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64");
        Pupuk pupukUrea = new Pupuk("PPK-101", "Pupuk Urea Subsidi", 350000, 40, "Urea");
        AlatPertanian cangkul = new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja");

        System.out.println("===== INFORMASI PRODUK AGRI-POS =====");
        
        // Menampilkan data dasar (Tugas 3)
        System.out.println("\n--- Data Dasar Produk ---");
        System.out.println("Benih: " + benihPadi.getNama() + " | Varietas: " + benihPadi.getVarietas());
        System.out.println("Pupuk: " + pupukUrea.getNama() + " | Jenis: " + pupukUrea.getJenis());
        System.out.println("Alat: " + cangkul.getNama() + " | Material: " + cangkul.getMaterial());

        // Menampilkan data lengkap (Latihan Mandiri)
        System.out.println("\n--- Deskripsi Lengkap Produk (Latihan Mandiri) ---");
        System.out.println("Deskripsi Benih : " + benihPadi.deskripsi());
        System.out.println("Deskripsi Pupuk : " + pupukUrea.deskripsi());
        System.out.println("Deskripsi Alat  : " + cangkul.deskripsi());

        // Menampilkan credit (Tugas 4)
        // Ganti dengan NIM dan Nama Anda
        CreditBy.print("240202863", "HANDIKA DWI ARDIYANTO");
    }
}
```

---

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
![Screenshot hasil](/praktikum/week3-inheritance/screenshots/Screenshot%202025-10-22%20115529%20week%203.png)
)
---

## Analisis
(
Program ini menunjukkan penerapan inheritance, di mana class Benih, Pupuk, dan AlatPertanian mewarisi atribut dan method dari class Produk.

Keyword super digunakan untuk mengakses konstruktor dan method milik superclass.

Dengan inheritance, atribut umum (kode, nama, harga, stok) tidak perlu ditulis ulang di setiap subclass.

Perbedaan dengan pendekatan class tunggal: inheritance membuat kode lebih ringkas dan mudah dikelola.

Kendala: sempat terjadi error karena lupa menuliskan super() dalam konstruktor subclass, tetapi sudah diperbaiki.
)
---

## Kesimpulan
Dengan menerapkan inheritance, struktur program menjadi lebih efisien, modular, dan mudah dikembangkan. Subclass dapat menggunakan kembali atribut dan method dari superclass tanpa perlu menduplikasi kode.*

---

## Quiz
Apa keuntungan menggunakan inheritance dibanding membuat class terpisah tanpa hubungan?
Jawaban: Mengurangi duplikasi kode dan membuat program lebih mudah dikembangkan karena subclass dapat mewarisi atribut dan method dari superclass.

Bagaimana cara subclass memanggil konstruktor superclass?
Jawaban: Dengan menggunakan keyword super() di dalam konstruktor subclass.

Berikan contoh kasus di POS pertanian selain Benih, Pupuk, dan Alat Pertanian yang bisa dijadikan subclass.
Jawaban: Contohnya Pestisida, ObatTanaman, atau MesinPanen yang bisa mewarisi atribut dari class Produk.
