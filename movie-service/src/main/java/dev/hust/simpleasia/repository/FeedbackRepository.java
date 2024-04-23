package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
}
