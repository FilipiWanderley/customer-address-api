package com.filipi.customeraddress.exception;

public class CepNotFoundException extends RuntimeException {

    public CepNotFoundException(String zipCode) {
        super("No address found for ZIP code: " + zipCode);
    }
}
