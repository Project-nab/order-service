package com.icomerce.shopping.order.services.imp;

import com.icomerce.shopping.order.clients.CartClient;
import com.icomerce.shopping.order.clients.ProductClient;
import com.icomerce.shopping.order.entities.CartStatus;
import com.icomerce.shopping.order.entities.Order;
import com.icomerce.shopping.order.entities.Shipment;
import com.icomerce.shopping.order.exception.InvalidOrderIdException;
import com.icomerce.shopping.order.exception.UpdateCartStatusException;
import com.icomerce.shopping.order.exception.UpdateProductQuantityException;
import com.icomerce.shopping.order.payload.request.CartRequest;
import com.icomerce.shopping.order.payload.request.ProductRequest;
import com.icomerce.shopping.order.payload.response.CartItemClientResponse;
import com.icomerce.shopping.order.repositories.OrderRepo;
import com.icomerce.shopping.order.repositories.ShipmentRepo;
import com.icomerce.shopping.order.services.ShipmentService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ShipmentServiceImp implements ShipmentService {
    private final ShipmentRepo shipmentRepo;
    private final OrderRepo orderRepo;
    private final CartClient cartClient;
    private final ProductClient productClient;

    @Autowired
    public ShipmentServiceImp(ShipmentRepo shipmentRepo,
                              OrderRepo orderRepo,
                              CartClient cartClient,
                              ProductClient productClient) {
        this.shipmentRepo = shipmentRepo;
        this.orderRepo = orderRepo;
        this.cartClient = cartClient;
        this.productClient = productClient;
    }

    @Override
    public Shipment createShipment(String address, String phoneNumber, Long orderId) throws InvalidOrderIdException,
            UpdateProductQuantityException, UpdateCartStatusException {
        Optional<Order> order = orderRepo.findById(orderId);
        log.info("Creating shipment - start");
        if(order.isPresent()) {
            log.info("Update product quantity");
            CartItemClientResponse cartItemClientResponse = cartClient.getCartItem(order.get().getCartSessionId());
            CartItemClientResponse.CartItem cartItems = cartItemClientResponse.getResult();
            for(CartItemClientResponse.CartItem.Content item : cartItems.getContent()) {
                ProductRequest productRequest = new ProductRequest();
                productRequest.setProductCode(item.getProductCode());
                productRequest.setQuantity(item.getQuantity());
                try {
                    productClient.updateProductQuantity(item.getProductCode(), productRequest);
                } catch(FeignException.BadRequest e) {
                    throw new UpdateProductQuantityException("Update product quantity exception " + item.getProductCode());
                }
            }
            log.info("Update cart status");
            try {
                cartClient.updateCart(order.get().getCartSessionId(), new CartRequest(null, 0, CartStatus.DELIVERING));
            } catch (FeignException.BadRequest e) {
                throw new UpdateCartStatusException("Update cart status exception " + order.get().getCartSessionId());
            }
            Shipment shipment = new Shipment();
            shipment.setAddress(address);
            shipment.setPhoneNumber(phoneNumber);
            shipment.setOrder(order.get());
            return shipmentRepo.save(shipment);
        }
        throw new InvalidOrderIdException("Invalid order id exception " + orderId);
    }
}
