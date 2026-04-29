package com.filipi.customeraddress.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone must contain 10 or 11 digits")
    private String phone;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{8}$", message = "ZIP code must contain exactly 8 digits")
    private String zipCode;

    private String number;

    private String complement;
}
