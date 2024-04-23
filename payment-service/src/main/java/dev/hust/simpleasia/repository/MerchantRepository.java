package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, String> {

}
