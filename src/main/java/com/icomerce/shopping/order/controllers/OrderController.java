package com.icomerce.shopping.order.controllers;

import com.icomerce.shopping.order.entities.Order;
import com.icomerce.shopping.order.exception.InvalidCartException;
import com.icomerce.shopping.order.exception.InvalidCartStatusException;
import com.icomerce.shopping.order.payload.request.CreateOrderRequest;
import com.icomerce.shopping.order.payload.response.BaseResponse;
import com.icomerce.shopping.order.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final HttpServletResponse response;

    @Autowired
    public OrderController(OrderService orderService,
                           HttpServletResponse response) {
        this.orderService = orderService;
        this.response = response;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public BaseResponse createOrder(@RequestBody CreateOrderRequest request,
                                    Principal principal) {
        String username = principal.getName();
        Order order = null;
        try {
            order = orderService.createOrder(request.getSessionId(), username);
            return new BaseResponse(HttpServletResponse.SC_CREATED, "Success", order);
        } catch (InvalidCartException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("Invalid cart exception ", e);
            return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), order);
        } catch (InvalidCartStatusException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("Invalid cart status exception ", e);
            return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), order);
        }
    }
}
