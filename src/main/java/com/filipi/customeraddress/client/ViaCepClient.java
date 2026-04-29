package com.filipi.customeraddress.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.exception.CepNotFoundException;
import com.filipi.customeraddress.exception.CepServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViaCepClient implements CepClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${cep.api.base-url}")
    private String baseUrl;

    @Override
    public CepResponse findByZipCode(String zipCode) {
        String url = baseUrl + "/" + zipCode + "/json";
        log.debug("Calling CEP API for zip code: {}", zipCode);

        try {
            CepResponse response = restTemplate.getForObject(url, CepResponse.class);

            if (response == null || response.hasError()) {
                throw new CepNotFoundException(zipCode);
            }

            return response;

        } catch (CepNotFoundException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            log.error("HTTP error while fetching CEP {}: {}", zipCode, e.getStatusCode());
            throw new CepServiceException("CEP service returned error: " + e.getStatusCode());
        } catch (Exception e) {
            log.error("Unexpected error while fetching CEP {}: {}", zipCode, e.getMessage());
            throw new CepServiceException("Unable to reach CEP service");
        }
    }
}
