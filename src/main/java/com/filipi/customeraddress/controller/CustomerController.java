package com.filipi.customeraddress.controller;

import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;
import com.filipi.customeraddress.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Registers a customer and automatically fills address data from the provided ZIP code")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find customer by ID")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all customers")
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates customer data. Address is re-fetched if ZIP code changes")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
