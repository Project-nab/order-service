package com.icomerce.shopping.order.exception;

public class InvalidCartException extends Exception {
    public InvalidCartException(String error) {
        super(error);
    }
}
