package com.upb.agripos;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CartServiceTest {

    @Test
    public void testCalculateTotal() {
        try {
            CartService cartService = new CartService();
            Product p1 = new Product("A1", "Test1", 10000, 10);
            Product p2 = new Product("A2", "Test2", 5000, 10);

            cartService.addToCart(p1, 2); // 20.000
            cartService.addToCart(p2, 1); // 5.000

            double total = cartService.calculateTotal();
            Assertions.assertEquals(25000.0, total, "Total harusnya 25.000");
        } catch (Exception e) {
            Assertions.fail("Exception tidak boleh terjadi: " + e.getMessage());
        }
    }
}