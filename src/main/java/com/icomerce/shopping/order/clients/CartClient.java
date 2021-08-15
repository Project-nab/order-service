package com.icomerce.shopping.order.clients;

import com.icomerce.shopping.order.payload.request.CartRequest;
import com.icomerce.shopping.order.payload.response.CartClientResponse;
import com.icomerce.shopping.order.payload.response.CartItemClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "cart-service")
public interface CartClient {
    @RequestMapping(method = RequestMethod.GET, value = "/cart-service/v1/carts/{sessionId}")
    CartClientResponse getCartBySessionId(@PathVariable(value = "sessionId") String sessionId);

    @RequestMapping(method = RequestMethod.PUT, value = "/cart-service/v1/carts/{sessionId}")
    CartClientResponse updateCart(@PathVariable(value = "sessionId") String sessionId, @RequestBody CartRequest request);

    @RequestMapping(method = RequestMethod.GET, value = "/cart-service/v1/carts/{sessionId}/items")
    CartItemClientResponse getCartItem(@PathVariable(value = "sessionId") String sessionId);
}
