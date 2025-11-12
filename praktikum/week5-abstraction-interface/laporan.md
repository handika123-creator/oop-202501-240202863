# Laporan Praktikum Minggu 5
Topik: Abstraction (Abstract Class & Interface)

## Identitas
- Nama  : HANDIKA DWI ARDIYANTO
- NIM   : 240202863
- Kelas : 3IKRA

---

## Tujuan
~Mahasiswa mampu menjelaskan perbedaan antara abstract class dan interface.
~Mahasiswa mampu mendesain abstract class dengan method abstrak sesuai kebutuhan.
~Mahasiswa mampu membuat dan mengimplementasikan interface pada class.
~Mahasiswa mampu menerapkan multiple inheritance melalui interface.
~Mahasiswa mampu mendokumentasikan kode dengan baik.

---

## Dasar Teori
1. Abstraksi adalah proses menyederhanakan kompleksitas sistem dengan menampilkan elemen penting dan menyembunyikan detail implementasi.

2. Abstract class tidak dapat diinstansiasi, dapat memiliki method abstrak dan non-abstrak, serta dapat menyimpan state (field).

3. Interface berisi kumpulan kontrak method (tanpa implementasi konkret), mendukung multiple inheritance, dan sejak Java 8 dapat memiliki default method.

4. Gunakan abstract class jika ada perilaku atau data dasar yang perlu diwariskan, dan gunakan interface untuk mendefinisikan kemampuan lintas hierarki.

5. Dalam sistem Agri-POS, abstract class digunakan untuk model Pembayaran, sedangkan interface digunakan untuk fitur seperti Validatable dan Receiptable.

---

## Langkah Praktikum
1. Membuat abstract class Pembayaran dengan field invoiceNo dan total, serta method:
         ~biaya() (abstrak),
         ~prosesPembayaran() (abstrak),
         ~totalBayar() (konkrit).

2. Membuat subclass Cash dan EWallet yang meng-extend Pembayaran.

3. Membuat interface Validatable dan Receiptable.

4. Mengimplementasikan multiple inheritance dengan membuat EWallet mengimplementasikan kedua interface.

5. Membuat class utama MainAbstraction.java untuk menampilkan hasil proses pembayaran.

6. Menjalankan program dan mengambil screenshot hasil eksekusi.

7. Melakukan commit
---

## Kode Program  
Receiptable.java
```java
package main.java.com.upb.agripos.model.kontrak;

/**
 * Interface Receiptable - kontrak untuk mencetak struk pembayaran.
 */
public interface Receiptable {
    String cetakStruk();
}
```
Validatable.java
```java
package main.java.com.upb.agripos.model.kontrak;

/**
 * Interface Validatable - kontrak untuk proses validasi (contoh: OTP, PIN).
 */
public interface Validatable {
    boolean validasi();
}
```
Cash.java
```java
package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class Cash extends Pembayaran implements Receiptable {
    private double tunai;

    public Cash(String invoiceNo, double total, double tunai) {
        super(invoiceNo, total);
        this.tunai = tunai;
    }

    @Override
    public double biaya() {
        return 0.0;
    }

    @Override
    public boolean prosesPembayaran() {
        return tunai >= totalBayar();
    }

    @Override
    public String cetakStruk() {
        return String.format(
                "INVOICE %s | TOTAL: %.2f | BAYAR CASH: %.2f | KEMBALI: %.2f | STATUS: %s",
                invoiceNo, totalBayar(), tunai,
                Math.max(0, tunai - totalBayar()),
                prosesPembayaran() ? "BERHASIL" : "GAGAL"
        );
    }
}
```
EWallet.java
```java
package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Validatable;
import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class EWallet extends Pembayaran implements Validatable, Receiptable {
    private String akun;
    private String otp;

    public EWallet(String invoiceNo, double total, String akun, String otp) {
        super(invoiceNo, total);
        this.akun = akun;
        this.otp = otp;
    }

    @Override
    public double biaya() {
        return total * 0.015;
    }

    @Override
    public boolean validasi() {
        return otp != null && otp.length() == 6;
    }

    @Override
    public boolean prosesPembayaran() {
        return validasi();
    }

    @Override
    public String cetakStruk() {
        return String.format(
                "INVOICE %s | TOTAL+FEE: %.2f | E-WALLET: %s | STATUS: %s",
                invoiceNo, totalBayar(), akun,
                prosesPembayaran() ? "BERHASIL" : "GAGAL"
        );
    }
}
```
Pembayaran.java
```java
package main.java.com.upb.agripos.model.pembayaran;

public abstract class Pembayaran {
    protected String invoiceNo;
    protected double total;

    public Pembayaran(String invoiceNo, double total) {
        this.invoiceNo = invoiceNo;
        this.total = total;
    }

    public abstract double biaya();
    public abstract boolean prosesPembayaran();

    public double totalBayar() {
        return total + biaya();
    }

    public String getInvoiceNo() { return invoiceNo; }
    public double getTotal() { return total; }
}
```
TransferBank.java
```java
package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Validatable;
import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class TransferBank extends Pembayaran implements Validatable, Receiptable {
    private String rekening;
    private String otp;

    public TransferBank(String invoiceNo, double total, String rekening, String otp) {
        super(invoiceNo, total);
        this.rekening = rekening;
        this.otp = otp;
    }

    @Override
    public double biaya() {
        return 3500;
    }

    @Override
    public boolean validasi() {
        return otp != null && otp.matches("\\d{6}");
    }

    @Override
    public boolean prosesPembayaran() {
        return validasi();
    }

    @Override
    public String cetakStruk() {
        return String.format(
                "INVOICE %s | TOTAL+FEE: %.2f | REKENING: %s | STATUS: %s",
                invoiceNo, totalBayar(), rekening,
                prosesPembayaran() ? "BERHASIL" : "GAGAL"
        );
    }
}
```
CreditBy.java
```java
package main.java.com.upb.agripos.util;

public class CreditBy {
    public static void print(String nim, String nama) {
        System.out.println("\n---");
        System.out.println("Credit by: " + nim + " - " + nama);
    }
}
```
MainAbstraction.java
```java
package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.pembayaran.*;
import main.java.com.upb.agripos.model.kontrak.*;
import main.java.com.upb.agripos.util.CreditBy;

public class MainAbstraction {
    public static void main(String[] args) {
        Pembayaran cash = new Cash("INV-001", 100000, 120000);
        Pembayaran ewallet = new EWallet("INV-002", 150000, "user@ewallet", "123456");
        Pembayaran transfer = new TransferBank("INV-003", 200000, "9876543210", "654321");

        System.out.println(((Receiptable) cash).cetakStruk());
        System.out.println(((Receiptable) ewallet).cetakStruk());
        System.out.println(((Receiptable) transfer).cetakStruk());

        CreditBy.print("240202863", "HANDIKA DWI ARDIYANTO");
    }
}
```

---

## Hasil Eksekusi  
![Screenshot hasil](/praktikum/week5-abstraction-interface/screenshots/Screenshot%202025-11-12%20120024%20WEEK%205.png)

---

## Analisis
~Kode program menggunakan konsep abstraksi dengan abstract class Pembayaran untuk menyembunyikan detail implementasi.

~Cash dan EWallet mewarisi struktur umum pembayaran namun memiliki cara proses berbeda.

~Interface Validatable dan Receiptable memungkinkan kelas mengimplementasikan kemampuan tambahan tanpa harus berada dalam satu hierarki pewarisan.

~Pendekatan minggu ini memperkenalkan multiple inheritance melalui interface, berbeda dengan minggu sebelumnya yang hanya menggunakan inheritance tunggal.

~Kendala yang dihadapi: memastikan validasi OTP dan perhitungan total berjalan dengan benar; diatasi dengan pengecekan sederhana dan uji coba output.
---

## Kesimpulan
Dengan menerapkan konsep Abstraction, Abstract Class, dan Interface, program menjadi lebih modular, fleksibel, dan mudah diperluas.
Mahasiswa memahami perbedaan konsep antara keduanya serta dapat mengaplikasikan multiple inheritance dengan aman melalui penggunaan interface di Java.

---

## Quiz
1. Jelaskan perbedaan konsep dan penggunaan abstract class dan interface.
   **Jawaban:** Abstract class digunakan saat ada perilaku dasar dan data yang dapat diwariskan, sedangkan interface digunakan untuk mendefinisikan kontrak kemampuan lintas kelas yang tidak terkait secara hierarkis.  

2. Mengapa multiple inheritance lebih aman dilakukan dengan interface pada Java?
   **Jawaban:** Karena interface tidak membawa state (data), hanya kontrak perilaku, sehingga tidak terjadi konflik pewarisan antar superclass seperti pada multiple inheritance class. 

3. Pada contoh Agri-POS, bagian mana yang paling tepat menjadi abstract class dan mana yang menjadi interface? Jelaskan alasannya.
   **Jawaban:** Pembayaran menjadi abstract class karena memiliki atribut umum (invoiceNo, total) dan perilaku dasar (totalBayar). Validatable dan Receiptable menjadi interface karena hanya mendefinisikan perilaku tambahan (validasi dan cetak struk) yang bisa dimiliki oleh berbagai jenis pembayaran.