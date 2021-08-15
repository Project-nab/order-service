package com.icomerce.shopping.order.services;

import com.icomerce.shopping.order.entities.Shipment;
import com.icomerce.shopping.order.exception.InvalidOrderIdException;
import com.icomerce.shopping.order.exception.UpdateCartStatusException;
import com.icomerce.shopping.order.exception.UpdateProductQuantityException;

public interface ShipmentService {
    Shipment createShipment(String address, String phoneNumber, Long orderId) throws InvalidOrderIdException, UpdateProductQuantityException, UpdateCartStatusException;
}
