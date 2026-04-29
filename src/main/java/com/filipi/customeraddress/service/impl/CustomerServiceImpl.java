package com.filipi.customeraddress.service.impl;

import com.filipi.customeraddress.client.CepClient;
import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;
import com.filipi.customeraddress.exception.CepNotFoundException;
import com.filipi.customeraddress.exception.CepServiceException;
import com.filipi.customeraddress.exception.CustomerNotFoundException;
import com.filipi.customeraddress.exception.EmailAlreadyInUseException;
import com.filipi.customeraddress.mapper.CustomerMapper;
import com.filipi.customeraddress.model.Customer;
import com.filipi.customeraddress.repository.CustomerRepository;
import com.filipi.customeraddress.service.CepConsultationLogService;
import com.filipi.customeraddress.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CepClient cepClient;
    private final CepConsultationLogService cepConsultationLogService;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException(request.getEmail());
        }

        Customer customer = customerMapper.toEntity(request);
        CepResponse address = fetchAndLogCep(request.getZipCode());
        customerMapper.applyAddressFromCep(address, customer);

        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        log.info("Updating customer id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        boolean emailChanged = !customer.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailChanged && customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException(request.getEmail());
        }

        customerMapper.updateFromRequest(request, customer);

        if (!customer.getZipCode().equals(request.getZipCode())) {
            CepResponse address = fetchAndLogCep(request.getZipCode());
            customerMapper.applyAddressFromCep(address, customer);
        }

        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
        log.info("Customer id {} deleted", id);
    }

    @Cacheable(value = "cep", key = "#zipCode")
    private CepResponse fetchAndLogCep(String zipCode) {
        try {
            CepResponse response = cepClient.findByZipCode(zipCode);
            cepConsultationLogService.logSuccess(zipCode, response, HttpStatus.OK.value());
            return response;
        } catch (CepNotFoundException e) {
            cepConsultationLogService.logFailure(zipCode, e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
            throw e;
        } catch (CepServiceException e) {
            cepConsultationLogService.logFailure(zipCode, e.getMessage(), HttpStatus.BAD_GATEWAY.value());
            throw e;
        }
    }
}
