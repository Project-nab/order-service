package com.icomerce.shopping.order.exception;

public class InvalidCartStatusException extends Exception {
    public InvalidCartStatusException(String error) {
        super(error);
    }
}
