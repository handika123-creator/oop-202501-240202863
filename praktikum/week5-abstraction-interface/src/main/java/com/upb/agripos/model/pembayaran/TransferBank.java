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