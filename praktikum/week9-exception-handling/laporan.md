# Laporan Praktikum Minggu 9
Topik: Exception Handling, Custom Exception, dan Penerapan Design Pattern

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
1. Mahasiswa mampu membedakan antara Error (fatal) dan Exception (dapat ditangani).
2. Mahasiswa mampu mengimplementasikan blok try-catch untuk menangani kesalahan runtime.
3. Mahasiswa mampu membuat Custom Exception (InvalidQuantity, ProductNotFound, InsufficientStock) untuk menangani logika bisnis Agri-POS.
4. Mahasiswa mampu mengintegrasikan validasi exception ke dalam kelas ShoppingCart.

---

## Dasar Teori

1. Exception Handling: Mekanisme dalam Java untuk menangani kondisi abnormal saat runtime sehingga program tidak berhenti mendadak (crash).
2. Try-Catch Block: Blok try berisi kode yang berpotensi error, sedangkan blok catch menangkap dan menangani error tersebut.
3. Custom Exception: Exception yang didefinisikan sendiri oleh programmer dengan cara mewarisi (extends) class Exception, digunakan untuk menangani kasus spesifik bisnis (misal: Stok Habis).
4. Throw: Keyword untuk melempar exception secara manual ketika kondisi error terpenuhi.
---

## Langkah Praktikum

1. Membuat tiga class Custom Exception:
      InvalidQuantityException: Jika input jumlah <= 0.
      ProductNotFoundException: Jika menghapus produk yang tidak ada di keranjang.
      InsufficientStockException: Jika stok produk di gudang kurang dari permintaan.
2. Memperbarui class Product dengan menambahkan atribut stock dan method reduceStock.
3. Memperbarui class ShoppingCart dengan menambahkan logika validasi (if-throw) pada method tambah, hapus, dan checkout.
4. Membuat class MainExceptionDemo untuk mensimulasikan berbagai skenario error dan memastikan blok try-catch berfungsi menangkap pesan kesalahan.
5. Melakukan commit ke repository dengan pesan: week9-exception: implementasi custom exception pada shopping cart.
---

## Kode Program
InvalidQuantityException.java
```java
package main.java.com.upb.agripos;

public class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
```
ProductNotFoundException.java
```java
package main.java.com.upb.agripos;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
```
InsufficientStockException.java
```java
package main.java.com.upb.agripos;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
```
Product.java
```java
package main.java.com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;
    private int stock; // Properti baru

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    
    public void reduceStock(int qty) {
        this.stock -= qty;
    }
}
```
ShoppingCart.java
```java
package main.java.com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<Product, Integer> items = new HashMap<>();

    // Menambahkan produk dengan validasi Quantity
    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity tidak valid: " + qty + " (Harus > 0)");
        }
        items.put(p, items.getOrDefault(p, 0) + qty);
        System.out.println("Berhasil menambah: " + p.getName() + " (Qty: " + qty + ")");
    }

    // Menghapus produk dengan validasi Keberadaan Produk
    public void removeProduct(Product p) throws ProductNotFoundException {
        if (!items.containsKey(p)) {
            throw new ProductNotFoundException("Gagal hapus: Produk " + p.getName() + " tidak ada di keranjang.");
        }
        items.remove(p);
        System.out.println("Berhasil menghapus: " + p.getName());
    }

    // Checkout dengan validasi Stok
    public void checkout() throws InsufficientStockException {
        System.out.println("\n--- Proses Checkout ---");
        // 1. Cek stok dulu
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int qtyNeeded = entry.getValue();
            
            if (product.getStock() < qtyNeeded) {
                throw new InsufficientStockException(
                    "Stok kurang untuk: " + product.getName() + 
                    " (Stok: " + product.getStock() + ", Diminta: " + qtyNeeded + ")"
                );
            }
        }

        // 2. Jika semua aman, baru kurangi stok
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
            System.out.println("Checkout sukses: " + entry.getKey().getName() + " - Stok sisa: " + entry.getKey().getStock());
        }
    }
}
```
MainExceptionDemo.java
```java
package main.java.com.upb.agripos;

public class MainExceptionDemo {
    public static void main(String[] args) {
        System.out.println("Hello, I am HANDIKA DWI ARDIYANTO - 240202863 (Week9)");
        System.out.println("====================================================");

        ShoppingCart cart = new ShoppingCart();
        // Stok awal hanya 3
        Product p1 = new Product("P01", "Pupuk Organik", 25000, 3); 

        // SKENARIO 1: Menambah Quantity Negatif (Error)
        System.out.println("\n[Test 1] Input Quantity Negatif");
        try {
            cart.addProduct(p1, -5);
        } catch (InvalidQuantityException e) {
            System.err.println("TERTANGKAP: " + e.getMessage());
        }

        // SKENARIO 2: Menghapus Produk yang belum ada (Error)
        System.out.println("\n[Test 2] Hapus Produk Kosong");
        try {
            cart.removeProduct(p1);
        } catch (ProductNotFoundException e) {
            System.err.println("TERTANGKAP: " + e.getMessage());
        }

        // SKENARIO 3: Checkout melebihi stok (Error)
        System.out.println("\n[Test 3] Checkout Melebihi Stok");
        try {
            cart.addProduct(p1, 10); // Minta 10, padahal stok cuma 3
            cart.checkout();
        } catch (Exception e) {
            // Menangkap InvalidQuantity atau InsufficientStock
            System.err.println("TERTANGKAP: " + e.getMessage());
        }

        // SKENARIO 4: Normal (Sukses)
        System.out.println("\n[Test 4] Transaksi Normal");
        try {
            // Reset keranjang (manual logic untuk simulasi)
            cart = new ShoppingCart(); 
            cart.addProduct(p1, 2); // Beli 2, Stok 3 (Cukup)
            cart.checkout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Hasil Eksekusi

![Screenshot hasil](/praktikum/week9-exception-handling/screenshots/Screenshot%202025-12-31%20144645%20week%209.png)

---

## Analisis
Program tidak mengalami crash (berhenti paksa) meskipun saya memasukkan input yang salah (seperti quantity -5). Hal ini karena exception tersebut ditangkap oleh blok catch.
Penggunaan Custom Exception membuat pesan error menjadi sangat jelas dan spesifik ("Stok kurang", "Qty tidak valid"), berbeda dengan error bawaan Java (seperti NullPointerException) yang sulit dipahami oleh pengguna awam.
Pada method checkout, validasi stok dilakukan di awal sebelum stok benar-benar dikurangi. Ini menjaga konsistensi data (agar stok tidak berkurang sebagian jika ada item lain yang gagal).
---

## Kesimpulan
Penerapan Exception Handling sangat penting dalam pengembangan aplikasi POS untuk menjamin ketahanan (robustness) aplikasi. Dengan Custom Exception, kita bisa mengontrol alur program saat terjadi kesalahan logika bisnis (seperti stok habis) dan memberikan umpan balik yang informatif kepada pengguna.

---

## Quiz
1. Jelaskan perbedaan error dan exception.
   **Jawaban:** …  
   Error: Masalah fatal yang biasanya tidak bisa ditangani oleh program (contoh: OutOfMemoryError, hardware failure). Program biasanya harus berhenti.
   Exception: Kondisi abnormal yang bisa ditangani oleh program (contoh: FileNotFound, input salah). Program bisa lanjut berjalan jika ditangani dengan try-catch.

2. Apa fungsi finally dalam blok try–catch–finally?
   **Jawaban:** …  
   Blok finally adalah blok kode yang pasti dijalankan, baik terjadi exception maupun tidak. Biasanya digunakan untuk membersihkan resource, seperti menutup koneksi database atau menutup file.

3. Mengapa custom exception diperlukan?
   **Jawaban:** …  
   Karena exception bawaan Java (seperti IllegalArgumentException) terlalu umum. Custom Exception memungkinkan kita membuat nama error yang sesuai dengan bisnis kita (misal InsufficientStockException), sehingga kode lebih mudah dibaca dan debugging lebih cepat.

4. Berikan contoh kasus bisnis dalam POS yang membutuhkan custom exception.
   **Jawaban:** …  
   ExpiredProductException: Saat kasir mencoba scan barang kadaluarsa.
   PromotionNotValidException: Saat kode voucher yang dimasukkan tidak memenuhi syarat minimal belanja.
   PaymentFailedException: Saat saldo e-wallet tidak cukup.