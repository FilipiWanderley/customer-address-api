package com.filipi.customeraddress.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;
import com.filipi.customeraddress.exception.CustomerNotFoundException;
import com.filipi.customeraddress.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private CustomerService customerService;

    @Test
    @DisplayName("POST /api/v1/customers - should return 201 on success")
    void shouldReturn201WhenCustomerCreated() throws Exception {
        CustomerRequest request = buildRequest();
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setName("João Silva");

        when(customerService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    @DisplayName("POST /api/v1/customers - should return 400 when request is invalid")
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        CustomerRequest invalid = new CustomerRequest();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} - should return 404 when customer not found")
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        when(customerService.findById(99L)).thenThrow(new CustomerNotFoundException(99L));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    private CustomerRequest buildRequest() {
        CustomerRequest r = new CustomerRequest();
        r.setName("João Silva");
        r.setEmail("joao@email.com");
        r.setPhone("11987654321");
        r.setZipCode("01310100");
        r.setNumber("100");
        return r;
    }
}
