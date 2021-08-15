package com.icomerce.shopping.order.payload.request;

import com.icomerce.shopping.order.entities.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private String productCode;
    private int quantity = 0;
    private CartStatus cartStatus;
}
