package com.icomerce.shopping.order.service.imp;

import com.icomerce.shopping.order.clients.CartClient;
import com.icomerce.shopping.order.entities.CartStatus;
import com.icomerce.shopping.order.exception.InvalidCartException;
import com.icomerce.shopping.order.exception.InvalidCartStatusException;
import com.icomerce.shopping.order.payload.request.CartRequest;
import com.icomerce.shopping.order.repositories.OrderRepo;
import com.icomerce.shopping.order.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @Autowired
    public OrderRepo orderRepo;

    @Autowired
    public OrderService orderService;

    @Autowired
    public CartClient cartClient;

    @Before
    public void setup() {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setCartStatus(CartStatus.NEW);
        cartClient.updateCart("4EF9C11DD7E95AEA3505D0BF17F23DAC", cartRequest);
    }

    @Test
    public void whenCreateAnOrder_thenReturnAnOrder() throws InvalidCartStatusException, InvalidCartException {
        orderService.createOrder("4EF9C11DD7E95AEA3505D0BF17F23DAC", "baonc93@gmail.com");
        assertEquals(1, orderRepo.count());
    }

    @Test(expected = InvalidCartException.class)
    public void whenCreateAnInvalidCardOrder_throwException() throws InvalidCartStatusException, InvalidCartException {
        orderService.createOrder("8EF9C11DD7E95AEA3505D0BF17F23DAC", "baonc93@gmail.com");
    }
}
