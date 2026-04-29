package com.filipi.customeraddress.controller;

import com.filipi.customeraddress.client.CepClient;
import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.service.CepConsultationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/cep")
@RequiredArgsConstructor
@Tag(name = "CEP", description = "ZIP code lookup endpoint")
public class CepController {

    private final CepClient cepClient;
    private final CepConsultationLogService cepConsultationLogService;

    @GetMapping("/{zipCode}")
    @Operation(summary = "Look up an address by ZIP code")
    public ResponseEntity<CepResponse> findByZipCode(
            @PathVariable
            @Pattern(regexp = "^\\d{8}$", message = "ZIP code must contain exactly 8 digits")
            String zipCode) {

        CepResponse response = cepClient.findByZipCode(zipCode);
        cepConsultationLogService.logSuccess(zipCode, response, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}
