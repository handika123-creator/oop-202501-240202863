package test.java.com.upb.agripos;

import main.java.com.upb.agripos.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        // Arrange (Siapkan data)
        String kode = "P-99";
        String nama = "Bibit Unggul";

        // Act (Jalankan kode yang mau dites)
        Product p = new Product(kode, nama);

        // Assert (Pastikan hasilnya sesuai harapan)
        assertEquals(kode, p.getCode(), "Kode produk harus sesuai");
        assertEquals(nama, p.getName(), "Nama produk harus sesuai");
    }
}