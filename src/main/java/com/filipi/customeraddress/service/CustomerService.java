package com.filipi.customeraddress.service;

import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse create(CustomerRequest request);

    CustomerResponse findById(Long id);

    List<CustomerResponse> findAll();

    CustomerResponse update(Long id, CustomerRequest request);

    void delete(Long id);
}
