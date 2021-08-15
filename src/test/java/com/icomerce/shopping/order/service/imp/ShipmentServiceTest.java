package com.icomerce.shopping.order.service.imp;

import com.icomerce.shopping.order.clients.CartClient;
import com.icomerce.shopping.order.clients.ProductClient;
import com.icomerce.shopping.order.entities.CartStatus;
import com.icomerce.shopping.order.entities.Order;
import com.icomerce.shopping.order.entities.OrderStatus;
import com.icomerce.shopping.order.entities.Shipment;
import com.icomerce.shopping.order.exception.*;
import com.icomerce.shopping.order.payload.request.CartRequest;
import com.icomerce.shopping.order.payload.response.CartClientResponse;
import com.icomerce.shopping.order.payload.response.ProductClientResponse;
import com.icomerce.shopping.order.repositories.OrderRepo;
import com.icomerce.shopping.order.repositories.ShipmentRepo;
import com.icomerce.shopping.order.services.OrderService;
import com.icomerce.shopping.order.services.ShipmentService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShipmentServiceTest {
    @Autowired
    public OrderRepo orderRepo;

    @Autowired
    public ShipmentRepo shipmentRepo;

    @Autowired
    public CartClient cartClient;

    @Autowired
    public ProductClient productClient;

    @Autowired
    public ShipmentService shipmentService;

    @Autowired
    public OrderService orderService;

    @Before
    public void setup() {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setCartStatus(CartStatus.NEW);
        cartClient.updateCart("4EF9C11DD7E95AEA3505D0BF17F23DAC", cartRequest);
    }

    @After
    public void tearDown() {
        shipmentRepo.deleteAll();
    }

    @Test
    public void whenCreateShipment_thenReturnShipment() throws InvalidCartStatusException, InvalidCartException,
            UpdateCartStatusException, InvalidOrderIdException, UpdateProductQuantityException {
        // When
        Order order = orderService.createOrder("4EF9C11DD7E95AEA3505D0BF17F23DAC", "baonc93@gmail.com");
        Shipment shipment = shipmentService.createShipment("125 Dong Van Cong stress, District 2, HCM City",
                "0355961181", order.getId());
        // Then
        assertEquals(1, shipmentRepo.count());
    }

    @Test(expected = InvalidOrderIdException.class)
    public void whenCreateShipmentWrongOrderId_thenThrowException() throws UpdateCartStatusException,
            InvalidOrderIdException, UpdateProductQuantityException {
        // When
        Shipment shipment = shipmentService.createShipment("125 Dong Van Cong stress, District 2, HCM City",
                "0355961181", 353523L);
    }

    @Test
    public void whenCreateShipment_thenCartStatusChanged() throws InvalidCartStatusException, InvalidCartException,
            UpdateCartStatusException, InvalidOrderIdException, UpdateProductQuantityException {
        // When
        Order order = orderService.createOrder("4EF9C11DD7E95AEA3505D0BF17F23DAC", "baonc93@gmail.com");
        Shipment shipment = shipmentService.createShipment("125 Dong Van Cong stress, District 2, HCM City",
                "0355961181", order.getId());
        // Then
        CartClientResponse cartClientResponse = cartClient.getCartBySessionId("4EF9C11DD7E95AEA3505D0BF17F23DAC");
        assertEquals(CartStatus.DELIVERING, cartClientResponse.getResult().getCartStatus());
    }

    @Test
    public void whenCreateShipment_thenQuantityDeduct() throws InvalidCartStatusException, InvalidCartException,
            UpdateCartStatusException, InvalidOrderIdException, UpdateProductQuantityException {
        // When
        ProductClientResponse productClientResponse = productClient.getProduct("ADIDAS_TSHIRT_01");
        int beforeQuantity = productClientResponse.getResult().getQuantity();
        Order order = orderService.createOrder("4EF9C11DD7E95AEA3505D0BF17F23DAC", "baonc93@gmail.com");
        Shipment shipment = shipmentService.createShipment("125 Dong Van Cong stress, District 2, HCM City",
                "0355961181", order.getId());

        // Then
        productClientResponse = productClient.getProduct("ADIDAS_TSHIRT_01");
        int afterQuantity = productClientResponse.getResult().getQuantity();
        assertEquals(beforeQuantity - 1, afterQuantity);
    }
}
