package com.upb.agripos.service.payment;

public interface PaymentStrategy {
    void pay(double amount);
    String getMethodName();
}