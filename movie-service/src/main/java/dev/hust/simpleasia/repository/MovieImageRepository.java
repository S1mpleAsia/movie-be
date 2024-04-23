package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {
    List<MovieImage> findAllByMovieId(Long movieId);
}
