package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.MerchantCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantCustomerRepository extends JpaRepository<MerchantCustomer, String> {
    Optional<MerchantCustomer> findFirstByCustomerIdAndMerchantName(String customerId, String merchantName);
}
