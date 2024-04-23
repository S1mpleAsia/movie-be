package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByVoteOverallIsNull();
}
