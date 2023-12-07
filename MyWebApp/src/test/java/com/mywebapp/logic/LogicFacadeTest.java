//package com.mywebapp.logic;
//
//import com.mywebapp.logic.models.CartItem;
//import com.mywebapp.logic.models.Order;
//import com.mywebapp.logic.models.Product;
//import org.junit.jupiter.api.Test;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class LogicFacadeTest {
//
//    @Test
//    void setOrderOwner() {
//        UUID test_product_sku = UUID.randomUUID();
//        Product test_product = new Product(test_product_sku, "test_product", "a test product", "tester", "test1", 0.0);
//        UUID test_cart_id = UUID.randomUUID();
//        CartItem cartItem1 = new CartItem(test_product, test_cart_id);
//
//        Order order = new Order(-1, "test", "test_shipping_address", null, false, items);
//        assertEquals(0,0);
//    }
//
//    @Test
//    void setPasscode() {
//        assertEquals(0,0);
//    }
//}