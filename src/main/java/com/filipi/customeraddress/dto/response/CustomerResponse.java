package com.filipi.customeraddress.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
