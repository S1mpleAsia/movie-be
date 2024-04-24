package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findAllByMovieIdOrderByCreatedAtDesc(Long movieId, Pageable pageable);
}
