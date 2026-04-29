package com.filipi.customeraddress.mapper;

import com.filipi.customeraddress.client.dto.CepResponse;
import com.filipi.customeraddress.dto.request.CustomerRequest;
import com.filipi.customeraddress.dto.response.CustomerResponse;
import com.filipi.customeraddress.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "neighborhood", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerRequest request);

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(CustomerRequest request, @MappingTarget Customer customer);

    @Mapping(target = "street", source = "street")
    @Mapping(target = "neighborhood", source = "neighborhood")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    void applyAddressFromCep(CepResponse cepResponse, @MappingTarget Customer customer);
}
