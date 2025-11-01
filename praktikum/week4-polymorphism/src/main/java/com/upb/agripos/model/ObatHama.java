package main.java.com.upb.agripos.model;

public class ObatHama extends Produk {
    private String kandungan;

    public ObatHama(String kode, String nama, double harga, int stok, String kandungan) {
        super(kode, nama, harga, stok);
        this.kandungan = kandungan;
    }

    @Override
    public String getInfo() {
        return "Obat Hama: " + super.getInfo() + ", Kandungan: " + kandungan;
    }
}