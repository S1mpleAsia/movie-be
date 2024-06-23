package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
     Page<Feedback> findAllByMovieIdOrderByCreatedAtDesc(Long movieId, Pageable pageable);
     List<Feedback> findAllByMovieId(Long movieId);
     List<Feedback> findAllByUserId(String userId);
}
