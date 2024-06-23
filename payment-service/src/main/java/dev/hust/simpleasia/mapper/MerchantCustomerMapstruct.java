package dev.hust.simpleasia.mapper;


import dev.hust.simpleasia.entity.domain.MerchantCustomer;
import dev.hust.simpleasia.entity.dto.MerchantCustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MerchantCustomerMapstruct {
    MerchantCustomer toMerchantCustomer(MerchantCustomerDto merchantCustomerDto);
    MerchantCustomerDto toMerchantCustomerDto(MerchantCustomer merchantCustomer);
}
