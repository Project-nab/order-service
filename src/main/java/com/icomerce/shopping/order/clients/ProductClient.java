package com.icomerce.shopping.order.clients;

import com.icomerce.shopping.order.payload.request.ProductRequest;
import com.icomerce.shopping.order.payload.response.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product-service")
public interface ProductClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/product-service/v1/products/{productCode}")
    ProductClientResponse updateProductQuantity(@PathVariable(value = "productCode") String productCode, @RequestBody ProductRequest productRequest);

    @RequestMapping(method = RequestMethod.GET, value = "/product-service/v1/products/{productCode}")
    ProductClientResponse getProduct(@PathVariable(value = "productCode") String productCode);
}
