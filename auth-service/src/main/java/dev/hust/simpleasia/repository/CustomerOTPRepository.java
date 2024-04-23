package dev.hust.simpleasia.repository;


import dev.hust.simpleasia.entity.domain.CustomerOTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerOTPRepository extends JpaRepository<CustomerOTP, String> {
    Optional<CustomerOTP> findFirstByEmailOrderByTimestampDesc(String email);
    Optional<CustomerOTP> findFirstByEmail(String email);
}
