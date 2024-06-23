package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, String> {
    Optional<Purchase> findFirstByUserIdAndType(String userId, String type);
    Optional<Purchase> findFirstByUserId(String userId);
    @Query("SELECT p FROM Purchase p WHERE YEAR(p.createdAt) = :year")
    List<Purchase> findAllByYear(@Param("year") int year);

    @Query("SELECT p FROM Purchase p WHERE p.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    List<Purchase> findAllByDateRange(@Param("startOfWeek") Date startOfWeek, @Param("endOfWeek") Date endOfWeek);
}
