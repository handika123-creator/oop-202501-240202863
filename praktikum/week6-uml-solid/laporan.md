# Laporan Praktikum Minggu 6
Topik: Desain Arsitektur Sistem dengan UML dan Prinsip SOLID

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
1. Mahasiswa mampu mengidentifikasi kebutuhan sistem Agri-POS dan memetakannya ke dalam diagram UML.

2. Mahasiswa mampu merancang arsitektur sistem menggunakan Use Case, Activity, Sequence, dan Class Diagram.

3. Mahasiswa mampu menjelaskan dan menerapkan prinsip desain SOLID (terutama SRP, OCP, dan DIP) untuk menciptakan sistem yang modular dan mudah dikembangkan.

---

## Dasar Teori 
1. UML (Unified Modeling Language) adalah standar visualisasi untuk merancang sistem berorientasi objek. 
2. Use Case Diagram menggambarkan fungsionalitas sistem dari sudut pandang aktor.
3. Activity Diagram memodelkan alur kerja (workflow) proses bisnis.
4. Sequence Diagram menggambarkan interaksi antar objek berdasarkan urutan waktu.
5. Class Diagram menunjukkan struktur statis sistem, atribut, metode, dan hubungan antar kelas.
6. SOLID Principles adalah 5 prinsip desain OOP untuk maintainability:
      -Single Responsibility Principle.
      -Open/Closed Principle.
      -Liskov Substitution Principle.
      -Interface Segregation Principle.
      -Dependency Inversion Principle.

---

## Langkah Praktikum
1. Menganalisis kebutuhan fungsional sistem Agri-POS (Manajemen Produk, Checkout, Pembayaran).
2. Membuat Use Case Diagram untuk mendefinisikan aktor (Admin, Kasir) dan fitur.
3. Membuat Activity Diagram untuk alur proses Checkout dengan penanganan error.
4. Membuat Sequence Diagram untuk detail interaksi objek pada proses pembayaran.
5. Merancang Class Diagram yang mengintegrasikan kode praktikum minggu 1-5 dengan tambahan layer Service dan Repository sesuai prinsip SOLID.
6. Menuliskan kode diagram menggunakan PlantUML.
7. Melakukan render diagram ke format .png dan menyimpannya di folder docs/.
8. Commit ke repository dengan pesan: week6-uml-solid: final design agri-pos.

---

## Kode Program
 
Use Case Diagram
```java
@startuml
left to right direction
actor "Admin" as A
actor "Kasir" as K

package "Agri-POS System" {
    usecase "Login" as UC1
    usecase "Kelola Data Produk\n(Benih, Pupuk, Alat, Obat)" as UC2
    usecase "Lihat Laporan Penjualan" as UC3
    usecase "Kelola Transaksi / Checkout" as UC4
    usecase "Proses Pembayaran\n(Tunai, E-Wallet)" as UC5
    usecase "Cetak Struk" as UC6
}

A --> UC1
A --> UC2
A --> UC3

K --> UC1
K --> UC4
K --> UC5
UC5 ..> UC6 : <<include>>
@enduml
```
Activity Diagram (Checkout)
```java
@startuml
|Kasir|
start
:Pilih Produk;
:Input Produk ke Keranjang;
repeat
    :Tambah produk lain?;
    if (Ya) then (ya)
        :Input Produk ke Keranjang;
    else (tidak)
        break
    endif
repeat while (Produk bertambah)

:Konfirmasi Checkout;

|Sistem Agri-POS|
:Hitung Total Belanja;
:Tampilkan Total & Pilihan Bayar;

|Kasir|
:Pilih Metode Pembayaran;

if (Metode Bayar?) then (Tunai)
    :Input Nominal Uang;
    |Sistem Agri-POS|
    if (Uang Cukup?) then (Ya)
        :Proses Transaksi;
    else (Tidak)
        :Tampilkan Error\n"Uang Kurang";
        stop
    endif
else (E-Wallet)
    |Kasir|
    :Input No HP & OTP;
    |External Payment Gateway|
    :Validasi Akun & Saldo;
    if (Valid?) then (Ya)
        |Sistem Agri-POS|
        :Terima Konfirmasi Sukses;
    else (Tidak)
        |Sistem Agri-POS|
        :Tampilkan Error\n"Gagal Validasi";
        stop
    endif
endif

|Sistem Agri-POS|
:Kurangi Stok Produk;
:Simpan Data Transaksi;
:Cetak Struk;

|Kasir|
:Terima Struk;
stop
@enduml
```
Class Diagram (Integrasi SOLID)
```java
@startuml

package "com.upb.agripos.model" {
    class Produk {
        -String kode
        -String nama
        -double harga
        -int stok
        +tambahStok(int)
        +kurangiStok(int)
        +getInfo() String
    }

    class Benih extends Produk {
        -String varietas
        +getInfo()
    }
    class Pupuk extends Produk {
        -String jenis
        +getInfo()
    }
    class AlatPertanian extends Produk {
        -String material
        +getInfo()
    }
    class ObatHama extends Produk {
        -String kandungan
        +getInfo()
    }
}

package "com.upb.agripos.payment" {
    interface PaymentMethod {
        +pay(amount: double) : boolean
        +biaya() : double
    }

    interface Receiptable {
        +cetakStruk() : String
    }
    
    interface Validatable {
        +validasi() : boolean
    }

    class CashPayment implements PaymentMethod, Receiptable {
        -double cashAmount
        +pay()
        +cetakStruk()
    }

    class EWalletPayment implements PaymentMethod, Receiptable, Validatable {
        -String phone
        -String otp
        +pay()
        +cetakStruk()
        +validasi()
    }
}

package "com.upb.agripos.service" {
    class TransactionService {
        -PaymentMethod method
        -ProductRepository repo
        +setPaymentMethod(PaymentMethod)
        +checkout()
    }
    
    interface ProductRepository {
        +save(Produk)
        +findById(String) : Produk
        +update(Produk)
    }
}

package "com.upb.agripos.repository" {
    class PostgresProductRepository implements ProductRepository {
        +save(Produk)
        +findById(String)
        +update(Produk)
    }
}

' Relasi SOLID
TransactionService --> PaymentMethod : uses (OCP)
TransactionService --> ProductRepository : depends on (DIP)
TransactionService ..> Produk : manages

@enduml
```
Sequence
```java
@startuml
autonumber
actor Kasir as K
boundary CheckoutUI as UI
control TransactionService as TS
participant "PaymentMethod\n<<Interface>>" as PM
entity ProductRepository as PR

K -> UI : Klik "Bayar" (Total: 150.000)
UI -> TS : processTransaction(orderId, paymentType)
activate TS

note right of TS
  Factory Pattern atau IF Logic 
  memilih strategi pembayaran
  (Cash atau EWallet)
end note

TS -> TS : getPaymentStrategy(paymentType)
TS -> PM : pay(amount)
activate PM

alt Pembayaran Sukses
    PM --> TS : return true
    
    loop for each item
        TS -> PR : updateStock(itemId, qty)
        activate PR
        PR --> TS : done
        deactivate PR
    end
    
    TS -> PR : saveTransaction(record)
    activate PR
    PR --> TS : saved
    deactivate PR
    
    TS --> UI : Transaksi Berhasil
    UI --> K : Tampilkan Struk
else Pembayaran Gagal
    PM --> TS : return false
    deactivate PM
    TS --> UI : Error: Pembayaran Gagal
    UI --> K : Tampilkan Pesan Kesalahan
end

deactivate TS
@enduml
```

---

## Hasil Eksekusi

![Screenshot hasil](/praktikum/week6-uml-solid/docs/uml_activity.png)

![Screenshot hasil](/praktikum/week6-uml-solid/docs/uml_class.png)

![Screenshot hasil](/praktikum/week6-uml-solid/docs/uml_sequence.png)

![Screenshot hasil](/praktikum/week6-uml-solid/docs/uml_usecase.png)

---

## Analisis
1. Penerapan SOLID Principles:

      Single Responsibility Principle (SRP): Saya memisahkan logika bisnis transaksi ke TransactionService dan akses data ke ProductRepository. Class Produk murni hanya menyimpan atribut data barang.

      Open/Closed Principle (OCP): Fitur pembayaran dirancang menggunakan interface PaymentMethod. Jika ingin menambah metode "Transfer Bank", saya cukup membuat class baru tanpa mengubah kode di TransactionService.

      Dependency Inversion Principle (DIP): TransactionService bergantung pada abstraksi (Interface ProductRepository), bukan pada implementasi konkret database. Ini memudahkan pengujian (testing).

      Interface Segregation Principle (ISP): Interface Validatable dipisahkan dari Receiptable sehingga CashPayment tidak terpaksa mengimplementasikan metode validasi OTP yang tidak diperlukannya.

2. Perbandingan dengan Minggu Sebelumnya: Minggu sebelumnya fokus pada implementasi koding per modul (Inheritance, Polymorphism). Minggu ini fokus pada gambaran besar (Big Picture) sistem, bagaimana modul-modul tersebut berinteraksi dalam satu arsitektur yang utuh.
---

## Kesimpulan
Desain arsitektur menggunakan UML sangat membantu dalam memvisualisasikan sistem sebelum coding dimulai. Penerapan prinsip SOLID, khususnya pemisahan tanggung jawab (SRP) dan penggunaan Interface (OCP & DIP), menghasilkan struktur sistem Agri-POS yang modular, fleksibel terhadap perubahan (misal penambahan metode bayar), dan mudah diuji.

---

## Quiz
1. Jelaskan perbedaan aggregation dan composition serta berikan contoh penerapannya pada desain Anda.  
   **Jawaban:** 
   Composition adalah hubungan "part-of" yang kuat; jika induk hancur, anak hancur. Contoh: Hubungan Transaksi dengan ItemTransaksi. Item tidak bisa berdiri sendiri tanpa transaksi induknya.

   Aggregation adalah hubungan "has-a" yang lemah; anak bisa hidup tanpa induk. Contoh: Hubungan Transaksi dengan Kasir. Jika data transaksi dihapus, data user Kasir tetap ada.

2. Bagaimana prinsip Open/Closed dapat memastikan sistem mudah dikembangkan?
   **Jawaban:**
   Prinsip OCP memungkinkan kita menambah fitur baru (seperti jenis pembayaran baru) hanya dengan menambahkan class baru (extension), tanpa harus mengedit atau memodifikasi kode lama yang sudah berjalan stabil. Ini mengurangi risiko kerusakan (bug) pada fitur yang sudah ada.

3. Mengapa Dependency Inversion Principle (DIP) meningkatkan testability? Berikan contoh penerapannya.  
   **Jawaban:**
   Karena module tingkat tinggi bergantung pada interface, bukan class konkret. Saat testing, kita bisa mengganti database asli dengan Mock Object (database tiruan di memori). Contoh: TransactionService bergantung pada ProductRepository. Saat unit test, kita menyuntikkan FakeProductRepository agar tes berjalan cepat tanpa perlu koneksi database PostgreSQL sungguhan.