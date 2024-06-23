package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.Genre;
import dev.hust.simpleasia.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public GeneralResponse<List<Genre>> getAllGenres() {
        List<Genre> genreList = genreRepository.findAll();

        return GeneralResponse.success(genreList);
    }

    public List<Genre> getGenreFromMovie(String genres) {
        List<Long> genreListId = Stream.of(genres.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return genreRepository.findByIdIn(genreListId);
    }

    public List<Genre> findAllGenreByNames(List<String> genreNameList) {
        return genreRepository.findAllByNameIn(genreNameList);
    }
}
