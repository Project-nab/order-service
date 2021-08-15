package com.icomerce.shopping.order.services;

import com.icomerce.shopping.order.entities.Order;
import com.icomerce.shopping.order.exception.InvalidCartException;
import com.icomerce.shopping.order.exception.InvalidCartStatusException;

public interface OrderService {
    Order createOrder(String cartSessionId, String username) throws InvalidCartException, InvalidCartStatusException;
}
