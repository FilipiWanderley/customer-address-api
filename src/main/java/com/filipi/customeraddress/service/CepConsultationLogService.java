package com.filipi.customeraddress.service;

import com.filipi.customeraddress.client.dto.CepResponse;

public interface CepConsultationLogService {

    void logSuccess(String zipCode, CepResponse response, int httpStatus);

    void logFailure(String zipCode, String errorMessage, int httpStatus);
}
