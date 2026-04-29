package com.filipi.customeraddress.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.model.CepConsultationLog;
import com.filipi.customeraddress.repository.CepConsultationLogRepository;
import com.filipi.customeraddress.service.CepConsultationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CepConsultationLogServiceImpl implements CepConsultationLogService {

    private final CepConsultationLogRepository logRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void logSuccess(String zipCode, CepResponse response, int httpStatus) {
        String payload = serialize(response);
        CepConsultationLog entry = CepConsultationLog.builder()
                .zipCode(zipCode)
                .responsePayload(payload)
                .httpStatus(httpStatus)
                .success(true)
                .build();
        logRepository.save(entry);
        log.debug("CEP consultation logged for zip code: {}", zipCode);
    }

    @Override
    public void logFailure(String zipCode, String errorMessage, int httpStatus) {
        String payload = "{\"error\": \"" + errorMessage + "\"}";
        CepConsultationLog entry = CepConsultationLog.builder()
                .zipCode(zipCode)
                .responsePayload(payload)
                .httpStatus(httpStatus)
                .success(false)
                .build();
        logRepository.save(entry);
        log.warn("CEP consultation failure logged for zip code: {}", zipCode);
    }

    private String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize CEP response for logging", e);
            return "{}";
        }
    }
}
