package com.icomerce.shopping.order.controllers;

import com.icomerce.shopping.order.entities.Shipment;
import com.icomerce.shopping.order.exception.InvalidOrderIdException;
import com.icomerce.shopping.order.exception.UpdateCartStatusException;
import com.icomerce.shopping.order.exception.UpdateProductQuantityException;
import com.icomerce.shopping.order.payload.request.ShipmentRequest;
import com.icomerce.shopping.order.payload.response.BaseResponse;
import com.icomerce.shopping.order.services.ShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final HttpServletResponse response;

    @Autowired
    public ShipmentController(ShipmentService shipmentService,
                              HttpServletResponse response) {
        this.shipmentService = shipmentService;
        this.response = response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orders/{orderId}/shipments")
    public BaseResponse createShipment(@PathVariable(value = "orderId") Long orderId,
                                       @RequestBody ShipmentRequest shipmentRequest) {
        try {
            Shipment shipment = shipmentService.createShipment(shipmentRequest.getAddress(), shipmentRequest.getPhoneNumber(),
                    orderId);
            return new BaseResponse(HttpServletResponse.SC_CREATED, "Success", shipment);
        } catch (InvalidOrderIdException e) {
            log.error("Invalid order id exception ", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (UpdateProductQuantityException e) {
            log.error("Update product quantity exception ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (UpdateCartStatusException e) {
            log.error("Update cart status exception ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
