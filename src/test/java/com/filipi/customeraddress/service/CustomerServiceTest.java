package com.filipi.customeraddress.service;

import com.filipi.customeraddress.client.CepClient;
import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;
import com.filipi.customeraddress.exception.CepNotFoundException;
import com.filipi.customeraddress.exception.EmailAlreadyInUseException;
import com.filipi.customeraddress.mapper.CustomerMapper;
import com.filipi.customeraddress.model.Customer;
import com.filipi.customeraddress.repository.CustomerRepository;
import com.filipi.customeraddress.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CepClient cepClient;
    @Mock private CepConsultationLogService cepConsultationLogService;
    @Mock private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest request;
    private Customer customer;
    private CepResponse cepResponse;

    @BeforeEach
    void setUp() {
        request = new CustomerRequest();
        request.setName("João Silva");
        request.setEmail("joao@email.com");
        request.setPhone("11987654321");
        request.setZipCode("01310100");
        request.setNumber("100");

        customer = Customer.builder()
                .id(1L)
                .name("João Silva")
                .email("joao@email.com")
                .phone("11987654321")
                .zipCode("01310100")
                .build();

        cepResponse = new CepResponse();
        cepResponse.setCep("01310100");
        cepResponse.setStreet("Avenida Paulista");
        cepResponse.setNeighborhood("Bela Vista");
        cepResponse.setCity("São Paulo");
        cepResponse.setState("SP");
    }

    @Test
    @DisplayName("Should create customer successfully when email is unique and CEP is valid")
    void shouldCreateCustomerSuccessfully() {
        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(cepClient.findByZipCode(request.getZipCode())).thenReturn(cepResponse);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toResponse(customer)).thenReturn(new CustomerResponse());

        CustomerResponse response = customerService.create(request);

        assertThat(response).isNotNull();
        verify(customerRepository).save(customer);
        verify(cepConsultationLogService).logSuccess(eq(request.getZipCode()), eq(cepResponse), anyInt());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException when email is already registered")
    void shouldThrowWhenEmailAlreadyExists() {
        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(EmailAlreadyInUseException.class);

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should log failure and rethrow when CEP is not found")
    void shouldLogFailureAndRethrowWhenCepNotFound() {
        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(cepClient.findByZipCode(request.getZipCode())).thenThrow(new CepNotFoundException(request.getZipCode()));

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(CepNotFoundException.class);

        verify(cepConsultationLogService).logFailure(eq(request.getZipCode()), anyString(), anyInt());
        verify(customerRepository, never()).save(any());
    }
}
