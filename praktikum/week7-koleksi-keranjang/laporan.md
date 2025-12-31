# Laporan Praktikum Minggu 7
Topik: Collections dan Implementasi Keranjang Belanja

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
1. Mahasiswa mampu menjelaskan perbedaan konsep Collection utama dalam Java: List, Set, dan Map.
2. Mahasiswa mampu menggunakan ArrayList untuk menyimpan objek transaksi belanja secara dinamis.
3. Mahasiswa mampu mengimplementasikan operasi penambahan, penghapusan, dan perhitungan total harga dalam keranjang belanja.
Mahasiswa memahami penggunaan HashMap sebagai alternatif untuk mengelola jumlah barang (quantity) secara efisien.

---

## Dasar Teori

1. Collections Framework adalah arsitektur terpadu di Java untuk menyimpan dan memanipulasi sekumpulan objek
2. List (ArrayList) adalah koleksi terurut yang memperbolehkan duplikasi elemen. Sangat cocok untuk data yang diakses berdasarkan indeks atau urutan penyisipan.
3. Map (HashMap) adalah koleksi yang menyimpan data dalam pasangan Key-Value. Kunci (Key) harus unik, dan sangat cepat untuk operasi pencarian data.
4. Set (HashSet) adalah koleksi yang tidak memperbolehkan elemen duplikat dan tidak menjamin urutan data.

---

## Langkah Praktikum

1. Membuat class Product sebagai representasi entitas barang (immutable) dengan atribut kode, nama, dan harga.
2. Membuat class ShoppingCart menggunakan ArrayList untuk menampung produk. Fitur yang dibuat meliputi addProduct, removeProduct, printCart, dan getTotal.
3. Membuat class alternatif ShoppingCartMap (Opsional) menggunakan HashMap untuk menangani logika quantity (jumlah barang) agar item yang sama tidak muncul berulang kali, melainkan jumlahnya bertambah.
4. Membuat class MainCart sebagai driver program untuk mensimulasikan transaksi pembelian sesuai skenario.
5. Melakukan kompilasi, menjalankan program, dan mengambil screenshot hasil eksekusi.
6. Melakukan commit ke repository dengan pesan: week7-collections: implementasi shopping cart arraylist dan map.

---

## Kode Program

Product.java
```java
package main.java.com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
```
ShoppingCart.java (Implementasi ArrayList)
```java
package main.java.com.upb.agripos;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p) {
        items.add(p);
        System.out.println("Berhasil menambahkan: " + p.getName());
    }

    public void removeProduct(Product p) {
        if(items.remove(p)) {
            System.out.println("Berhasil menghapus: " + p.getName());
        } else {
            System.out.println("Barang tidak ditemukan.");
        }
    }

    public double getTotal() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void printCart() {
        System.out.println("\n--- Struk Belanja (List) ---");
        if (items.isEmpty()) {
            System.out.println("Keranjang kosong.");
        } else {
            for (Product p : items) {
                System.out.println("- " + p.getCode() + " " + p.getName() + " : Rp" + p.getPrice());
            }
            System.out.println("Total: Rp" + getTotal());
        }
        System.out.println("---------------------------");
    }
}
```
MainCart.java
```java
package main.java.com.upb.agripos;

public class MainCart {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week7)");
        System.out.println("====================================================");

        // Data Produk Dummy
        Product p1 = new Product("BNH-01", "Benih Padi", 50000);
        Product p2 = new Product("PPK-02", "Pupuk Urea", 30000);
        Product p3 = new Product("ALT-03", "Cangkul", 75000);

        // --- Skenario 1: Menggunakan ArrayList ---
        System.out.println("\nTESTING ARRAYLIST (Basic)");
        ShoppingCart listCart = new ShoppingCart();
        
        listCart.addProduct(p1); // Beli Benih
        listCart.addProduct(p2); // Beli Pupuk
        listCart.addProduct(p1); // Beli Benih lagi (List akan mencatatnya 2 baris terpisah)
        
        listCart.printCart();

        listCart.removeProduct(p2); // Hapus Pupuk
        listCart.printCart();

        // --- Skenario 2: Menggunakan HashMap (Advanced) ---
        System.out.println("\nTESTING HASHMAP (Quantity Logic)");
        ShoppingCartMap mapCart = new ShoppingCartMap();
        
        mapCart.addProduct(p1); // Qty jadi 1
        mapCart.addProduct(p1); // Qty jadi 2
        mapCart.addProduct(p3); // Qty jadi 1
        
        mapCart.printCart(); // Harusnya Benih x2
        
        mapCart.removeProduct(p1); // Qty Benih turun jadi 1
        mapCart.printCart();
    }
}
```
ShoppingCartMap.java
```java
package main.java.com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartMap {
    private final Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p) {
        items.put(p, items.getOrDefault(p, 0) + 1);
        System.out.println("[Map] +1 Qty untuk: " + p.getName());
    }

    public void removeProduct(Product p) {
        if (!items.containsKey(p)) return;
        
        int qty = items.get(p);
        if (qty > 1) {
            items.put(p, qty - 1);
            System.out.println("[Map] -1 Qty untuk: " + p.getName());
        } else {
            items.remove(p);
            System.out.println("[Map] Hapus item: " + p.getName());
        }
    }

    public double getTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void printCart() {
        System.out.println("\n--- Keranjang Belanja (Map/Qty) ---");
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            System.out.println("- " + e.getKey().getName() + " (x" + e.getValue() + ") : Rp" + (e.getKey().getPrice() * e.getValue()));
        }
        System.out.println("Total: Rp" + getTotal());
        System.out.println("-----------------------------------");
    }
}
```
---

## Hasil Eksekusi
 
![Screenshot hasil](/praktikum/week7-koleksi-keranjang/screenshots/Screenshot%202025-12-31%20134503%20week%207.png)

---

## Analisis
Cara Kode Berjalan: Class ShoppingCart menggunakan ArrayList<Product> untuk menyimpan barang. Ketika addProduct dipanggil, objek produk dimasukkan ke list. Ketika getTotal dipanggil, program melakukan looping (iterasi) seluruh isi list untuk menjumlahkan harga.

Perbedaan Pendekatan: Minggu sebelumnya fokus pada struktur arsitektur (UML & SOLID). Minggu ini fokus pada struktur data internal. Kita tidak lagi menggunakan Array statis (Product[]) yang ukurannya kaku, melainkan ArrayList yang dinamis (bisa bertambah/berkurang ukurannya otomatis).

Efisiensi List vs Map: Implementasi ArrayList mudah dibuat dan mempertahankan urutan input (cocok untuk struk). Namun, jika kita ingin menghitung jumlah item yang sama (quantity), ArrayList kurang efisien karena data tersimpan berulang-ulang. Solusinya adalah menggunakan HashMap dimana produk menjadi Key dan jumlahnya menjadi Value.
---

## Kesimpulan
Penggunaan Collections Framework, khususnya ArrayList, sangat memudahkan pengelolaan data transaksi dalam sistem Agri-POS karena sifatnya yang dinamis dibandingkan array biasa. Untuk kasus yang membutuhkan pengelolaan stok atau quantity yang unik, HashMap merupakan pilihan struktur data yang lebih tepat dan efisien.
---

## Quiz
1. Jelaskan perbedaan mendasar antara List, Map, dan Set. 
   **Jawaban:** …  
   List: Koleksi terurut yang mengizinkan elemen duplikat (contoh: antrian, daftar belanja).
   Map: Koleksi pasangan Key-Value, di mana Key harus unik (contoh: kamus, data stok).
   Set: Koleksi himpunan yang wajib unik/tidak boleh duplikat (contoh: daftar email unik).

2. Mengapa ArrayList cocok digunakan untuk keranjang belanja sederhana?
   **Jawaban:** …  
   Karena ArrayList mempertahankan urutan penyisipan (insertion order). Hal ini sesuai dengan perilaku kasir saat memindai barang satu per satu, dan struk belanja biasanya dicetak urut berdasarkan barang apa yang discan duluan.

3. Bagaimana struktur Set mencegah duplikasi data?
   **Jawaban:** …  
   Set menggunakan metode hashCode() dan equals() pada objek. Sebelum data dimasukkan, Set mengecek apakah kode hash objek tersebut sudah ada di dalam koleksi. Jika sudah ada, penambahan data baru akan ditolak.

4. Kapan sebaiknya menggunakan Map dibandingkan List? Jelaskan dengan contoh.
   **Jawaban:** …  
   Gunakan Map ketika membutuhkan akses data yang sangat cepat berdasarkan kunci unik, atau untuk memetakan hubungan antar objek.
      Contoh: Menyimpan stok gudang Agri-POS. Lebih baik menggunakan Map<String, Integer> (Kode Produk -> Jumlah Stok) daripada List. Dengan Map, kita bisa langsung mengambil sisa stok produk "BNH-01" tanpa harus mencari satu per satu di seluruh daftar.