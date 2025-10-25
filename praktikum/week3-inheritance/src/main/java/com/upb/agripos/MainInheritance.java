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