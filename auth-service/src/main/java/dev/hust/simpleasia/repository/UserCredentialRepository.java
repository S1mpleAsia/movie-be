package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
    Optional<UserCredential> findFirstByEmailAndStatus(String email, String status);
    Optional<UserCredential> findFirstByEmail(String email);
    @Query("SELECT u from UserCredential u where YEAR(u.createdAt) = :year")
    List<UserCredential> findAllByYear(@Param("year") int year);

    @Query("SELECT u from UserCredential u where u.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    List<UserCredential> findAllByDateRange(@Param("startOfWeek") Date startOfWeek, @Param("endOfWeek") Date endOfWeek);
}
