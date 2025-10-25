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