package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
    Optional<UserCredential> findFirstByEmailAndStatus(String email, String status);
    Optional<UserCredential> findFirstByEmail(String email);
}
