package dev.hust.simpleasia.service;

import dev.hust.simpleasia.entity.domain.MovieImage;
import dev.hust.simpleasia.repository.MovieImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieImageService {
    private final MovieImageRepository movieImageRepository;

    public List<MovieImage> getImageList(Long movieId) {
        return movieImageRepository.findAllByMovieId(movieId);
    }

    public void saveImage(MovieImage movieImage) {
        movieImageRepository.save(movieImage);
    }
}
