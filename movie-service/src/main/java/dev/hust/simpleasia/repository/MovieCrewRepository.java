package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCrewRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByMovieId(Long movieId);
    List<Credit> findAllByActorId(Long actorId);
}
