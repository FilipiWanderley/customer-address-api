package com.filipi.customeraddress.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.exception.CepNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ViaCepClientTest {

    private RestTemplate restTemplate;
    private ViaCepClient viaCepClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        viaCepClient = new ViaCepClient(restTemplate, new ObjectMapper());
        ReflectionTestUtils.setField(viaCepClient, "baseUrl", "http://localhost:8089/ws");
    }

    @Test
    @DisplayName("Should return CepResponse when API returns a valid address")
    void shouldReturnCepResponseOnSuccess() {
        CepResponse expected = new CepResponse();
        expected.setCep("01310100");
        expected.setStreet("Avenida Paulista");
        expected.setCity("São Paulo");
        expected.setState("SP");

        when(restTemplate.getForObject(anyString(), eq(CepResponse.class))).thenReturn(expected);

        CepResponse result = viaCepClient.findByZipCode("01310100");

        assertThat(result.getCep()).isEqualTo("01310100");
        assertThat(result.getCity()).isEqualTo("São Paulo");
    }

    @Test
    @DisplayName("Should throw CepNotFoundException when API returns erro=true")
    void shouldThrowWhenApiReturnsError() {
        CepResponse errorResponse = new CepResponse();
        errorResponse.setErro("true");

        when(restTemplate.getForObject(anyString(), eq(CepResponse.class))).thenReturn(errorResponse);

        assertThatThrownBy(() -> viaCepClient.findByZipCode("00000000"))
                .isInstanceOf(CepNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw CepNotFoundException when API returns null")
    void shouldThrowWhenApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(CepResponse.class))).thenReturn(null);

        assertThatThrownBy(() -> viaCepClient.findByZipCode("00000000"))
                .isInstanceOf(CepNotFoundException.class);
    }
}
