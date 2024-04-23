package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> getVideosByMovieId(Long movieId);
}
