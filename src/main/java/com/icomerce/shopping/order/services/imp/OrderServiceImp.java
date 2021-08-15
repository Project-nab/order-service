package com.icomerce.shopping.order.services.imp;

import com.icomerce.shopping.order.clients.CartClient;
import com.icomerce.shopping.order.entities.CartStatus;
import com.icomerce.shopping.order.entities.Order;
import com.icomerce.shopping.order.entities.OrderStatus;
import com.icomerce.shopping.order.exception.InvalidCartException;
import com.icomerce.shopping.order.exception.InvalidCartStatusException;
import com.icomerce.shopping.order.payload.response.CartClientResponse;
import com.icomerce.shopping.order.repositories.OrderRepo;
import com.icomerce.shopping.order.services.OrderService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderServiceImp implements OrderService {
    private final OrderRepo orderRepo;
    private final CartClient cartClient;

    @Autowired
    public OrderServiceImp(OrderRepo orderRepo, CartClient cartClient) {
        this.orderRepo = orderRepo;
        this.cartClient = cartClient;
    }

    @Override
    public Order createOrder(String cartSessionId, String username) throws InvalidCartException,
            InvalidCartStatusException {
        log.info("Creating an order - start");
        CartClientResponse cartClientResponse;
        try {
            cartClientResponse = cartClient.getCartBySessionId(cartSessionId);
        } catch (FeignException.BadRequest e) {
            throw new InvalidCartException("Invalid cart exception " + cartSessionId);
        }

        if(cartClientResponse.getResult().getCartStatus() != CartStatus.NEW) {
            throw new InvalidCartStatusException("Invalid cart status exception " + cartSessionId);
        }

        Order order = new Order();
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setCartSessionId(cartSessionId);
        order.setStatus(OrderStatus.NEW);
        order.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return orderRepo.save(order);
    }
}
