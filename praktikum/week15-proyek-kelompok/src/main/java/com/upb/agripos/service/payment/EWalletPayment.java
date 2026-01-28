package com.upb.agripos.service.payment;

public class EWalletPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Bayar via QRIS/E-Wallet: " + amount);
    }

    @Override
    public String getMethodName() {
        return "E-WALLET";
    }
}