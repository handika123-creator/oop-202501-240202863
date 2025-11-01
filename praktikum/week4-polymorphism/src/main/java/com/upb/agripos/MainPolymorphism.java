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