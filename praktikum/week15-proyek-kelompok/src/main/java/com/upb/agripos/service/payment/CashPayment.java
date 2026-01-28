package com.upb.agripos.service.payment;

public class CashPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Bayar Tunai: " + amount);
    }

    @Override
    public String getMethodName() {
        return "CASH";
    }
}