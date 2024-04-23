package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, String> {
}
