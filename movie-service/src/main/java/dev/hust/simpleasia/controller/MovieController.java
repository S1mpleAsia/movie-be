package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import dev.hust.simpleasia.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public GeneralResponse<List<Movie>> getMovieList(@RequestParam(value = "page", defaultValue = "1") Integer page) {
        return movieService.getMovieList(page);
    }

    @GetMapping("/{id}")
    public GeneralResponse<MovieDetailResponse> getMovieDetail(@PathVariable("id") Long id) {
        return movieService.getDetail(id);
    }

    @PostMapping("mock")
    public void mockFeedbackOverall() {
        movieService.mockFeedbackOverall();
    }
}
