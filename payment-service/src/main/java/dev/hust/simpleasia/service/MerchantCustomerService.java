package dev.hust.simpleasia.service;

import dev.hust.simpleasia.entity.domain.MerchantCustomer;
import dev.hust.simpleasia.entity.dto.MerchantCustomerDto;
import dev.hust.simpleasia.mapper.MerchantCustomerMapstruct;
import dev.hust.simpleasia.repository.MerchantCustomerRepository;
import dev.hust.simpleasia.utils.MerchantConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantCustomerService {
    private final MerchantCustomerRepository merchantCustomerRepository;
    private final MerchantCustomerMapstruct merchantCustomerMapstruct;

    public MerchantCustomerDto findCustomer(String customerId, MerchantConstant merchant) {
        MerchantCustomer merchantCustomer = merchantCustomerRepository.findFirstByCustomerIdAndMerchantName(customerId, merchant.name())
                .orElse(null);

        return merchantCustomerMapstruct.toMerchantCustomerDto(merchantCustomer);
    }

    public void save(MerchantCustomerDto merchantCustomerDto) {
        merchantCustomerRepository.save(merchantCustomerMapstruct.toMerchantCustomer(merchantCustomerDto));
    }
}
