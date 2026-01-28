# Laporan Praktikum Minggu 15
Topik: Aplikasi Point of Sales (POS) Toko Pertanian

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## 1. Identitas Kelompok
Berikut adalah anggota tim dan pembagian tugas dalam pengembangan aplikasi ini:

| NIM | Nama | Peran | Tanggung Jawab Utama |
| :--- | :--- | :--- | :--- |
| 240202849 | Ahmad Rafie Ramadhani A. | Project Manager | [cite_start]Arsitektur MVC & Integrasi Kode [cite: 10, 302] |
| 240202863 | Handika Dwi Ardiyanto | Frontend Dev | [cite_start]Desain GUI Login, Validasi Input & Inventory [cite: 10, 303] |
| 240202852 | Alvirdaus Permathasyahidatama | Frontend Dev | [cite_start]GUI Kasir (POS), Laporan & Payment Strategy [cite: 10, 304] |
| 240202835 | Febri Muhsinin | Backend/DB | [cite_start]Database Engine, ERD & DAO Layer [cite: 10, 303] |
| 240202880 | Rayendra Apta Nayottama | QA / Tester | [cite_start]Testing, Exception Handling & Dokumentasi [cite: 10, 304] |

---

## 2. Deskripsi Sistem
**Agri-POS** adalah aplikasi kasir berbasis desktop yang dirancang untuk toko pertanian (saprotan). Aplikasi ini menggantikan pencatatan manual (buku tulis) menjadi sistem digital yang terintegrasi database [cite: 19-25].

**Fitur Utama:**
1.  **Multi-Role Login:** Hak akses berbeda untuk Admin (Manajemen Stok) dan Kasir (Penjualan) [cite: 58-61].
2.  **Manajemen Inventaris:** Admin dapat melakukan CRUD (Create, Read, Update, Delete) data barang, harga, dan stok [cite: 69-71].
3.  **Transaksi Penjualan:** Kasir dapat memasukkan barang ke keranjang, sistem menghitung total otomatis, dan mengurangi stok secara real-time [cite: 74-78].
4.  **Laporan Riwayat:** Menampilkan log transaksi penjualan sebelumnya untuk keperluan audit[cite: 85].

---

## 3. Arsitektur & Desain (OOP)
Aplikasi ini dibangun menggunakan prinsip **OOP** yang ketat dengan arsitektur **MVC (Model-View-Controller)** [cite: 93-94]:

* **Model (Data Layer):** Merepresentasikan objek data seperti `User`, `Product`, `Transaction` [cite: 95-98].
* **View (Presentation Layer):** Menangani tampilan antarmuka JavaFX (contoh: `LoginView`, `PosView`) [cite: 98-102].
* **Controller (Logic Layer):** Mengatur logika bisnis dan menghubungkan View dengan Database [cite: 104-107].

**Design Pattern yang digunakan:**
1.  **DAO Pattern:** Memisahkan query SQL dari kode utama (`UserDAO`, `ProductDAO`) [cite: 110-113].
2.  **Singleton Pattern:** Menghemat memori dengan memastikan hanya ada satu koneksi database yang terbuka (`DatabaseConnection`) [cite: 115-118].
3.  **Strategy Pattern:** Memberikan fleksibilitas dalam memilih metode pembayaran (Tunai vs E-Wallet) tanpa mengubah logika inti [cite: 119-123].

---

## 4. Struktur Database
Database PostgreSQL (`agripos`) dirancang dengan normalisasi tingkat 3 (3NF) dan terdiri dari tabel berikut[cite: 134]:

1.  **`users`**: Menyimpan username, password, dan role (admin/kasir)[cite: 135].
2.  **`products`**: Menyimpan kode barang, nama, harga, dan stok[cite: 138].
3.  **`transactions`**: Header nota (ID, tanggal, total, kasir)[cite: 140].
4.  **`transaction_details`**: Rincian barang apa saja yang dibeli dalam satu transaksi[cite: 145].

---

## 5. Kontribusi Saya (Frontend Developer)
Sebagai Frontend Developer, fokus utama saya adalah memastikan antarmuka pengguna (GUI) berjalan lancar, responsif, dan valid[cite: 303].

**Tugas Spesifik:**
1.  **Membangun Login View:**
    * Membuat form login yang aman dengan pemisahan akses (Role-Based).
    * Menambahkan validasi input (tidak boleh kosong).
    * Mengintegrasikan tombol login dengan `UserDAO` untuk verifikasi kredensial.
2.  **Mengembangkan Inventory UI (Sisi Admin):**
    * Membuat tampilan tabel data barang (`TableView`) yang terhubung dengan database.
    * Membuat form input untuk menambah barang baru.
    * Menangani event handling (aksi klik tombol Simpan/Hapus).
3.  **User Experience (UX):**
    * Memastikan layout responsif menggunakan `BorderPane` dan `GridPane`[cite: 160].

---

## 6. Implementasi Kode
Berikut adalah cuplikan kode implementasi Login dan Koneksi Database yang menjadi pondasi aplikasi ini:

**A. Koneksi Database (Singleton)**
```java
// File: DatabaseConnection.java
public class DatabaseConnection {
    private static Connection connection;
    // Constructor private (Singleton)
    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                [cite_start]// Hanya membuka koneksi satu kali untuk efisiensi [cite: 324-333]
                connection = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
```
---

B. Logika Login (DAO)
```java
// File: UserDAO.java
public User login(String username, String password) throws SQLException {
    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
    // Menggunakan PreparedStatement agar aman dari SQL Injection [cite: 338-349]
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new User(rs.getString("username"), rs.getString("role"));
        }
    }
    return null;
}
```
---

## 7. Hasil & Dokumentasi
Berikut adalah tampilan aplikasi yang berhasil dijalankan beserta bukti struktur database:

   1. Halaman Login
      ![Screenshot hasil](/praktikum/week15-proyek-kelompok/screenshots/Screenshot%202026-01-26%20063808%20week%2015%20login.png)

2. Halaman Admin (Inventory)
      ![Screenshot hasil](/praktikum/week15-proyek-kelompok/screenshots/Screenshot%202026-01-26%20064005%20week%2015%20admin.png)

3. Halaman Kasir (Transaksi)
      ![Screenshot hasil](/praktikum/week15-proyek-kelompok/screenshots/Screenshot%202026-01-26%20162334%20week%2015%20kasir.png)
      
4. Bukti Database (PostgreSQL)
      ![Screenshot hasil](/praktikum/week15-proyek-kelompok/screenshots/Screenshot%202026-01-28%20203019%20week%2015%20database.png)

---

## 8. Kesimpulan
- Proyek Agri-POS berhasil mengintegrasikan materi OOP Semester 3 menjadi produk perangkat lunak yang fungsional.

- Arsitektur MVC terbukti memudahkan pembagian kerja dalam tim karena memisahkan tampilan (View) dari logika (Controller).

- Penerapan Design Pattern (Singleton & Strategy) membuat kode lebih efisien dan mudah dikembangkan di masa depan.

- Database PostgreSQL menjamin persistensi data yang lebih aman dibandingkan penyimpanan file manual.