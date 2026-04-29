package com.filipi.customeraddress.client;

import com.filipi.customeraddress.client.dto.CepResponse;

public interface CepClient {

    CepResponse findByZipCode(String zipCode);
}
