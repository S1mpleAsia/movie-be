package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, String> {

}
