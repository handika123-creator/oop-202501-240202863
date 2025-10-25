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