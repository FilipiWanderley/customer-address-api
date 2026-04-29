package com.filipi.customeraddress.service;

import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    CustomerResponse create(CustomerRequest request);

    CustomerResponse findById(Long id);

    List<CustomerResponse> findAll();

    Page<CustomerResponse> findAll(Pageable pageable);

    CustomerResponse update(Long id, CustomerRequest request);

    void delete(Long id);
}
