package com.filipi.customeraddress.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CepResponse {

    private String cep;

    @JsonProperty("logradouro")
    private String street;

    @JsonProperty("complemento")
    private String complement;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String state;

    private String erro;

    public boolean hasError() {
        return "true".equals(erro);
    }
}
