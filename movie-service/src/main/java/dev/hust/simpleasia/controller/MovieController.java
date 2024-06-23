package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.dto.*;
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

    @GetMapping("/all")
    public GeneralResponse<List<Movie>> getAllMovieList() {
        return movieService.getAllMovie();
    }

    @GetMapping("/top-rated")
    public GeneralResponse<List<Movie>> getTopRatedMovieList() {
        return movieService.getTopRatedMovieList();
    }

    @GetMapping("/{id}")
    public GeneralResponse<MovieDetailResponse> getMovieDetail(@PathVariable("id") Long id) {
        return movieService.getDetail(id);
    }

    @PostMapping("/status")
    public GeneralResponse<Movie> changeMovieStatus(@RequestBody MovieStatusRequest request) {
        return movieService.changeMovieStatus(request);
    }

    @PostMapping
    public GeneralResponse<MovieDetailResponse> createMovie(@RequestBody MovieInitRequest request) {
        return movieService.createMovie(request);
    }

    @PostMapping("modified")
    public GeneralResponse<Object> updateMovie(@RequestBody MovieModifiedRequest request) {
        return movieService.updateMovie(request);
    }

    @GetMapping("basic-info")
    public GeneralResponse<MovieModifiedResponse> getBasicInfo(@RequestParam("id") Long id) {
        return movieService.getBasicInfo(id);
    }


    @PostMapping("mock")
    public void mockFeedbackOverall() {
        movieService.mockFeedbackOverall();
    }

    @GetMapping("recommend")
    public GeneralResponse<List<Movie>> getRecommendMovie(@RequestParam("query") String query,
                                                          @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return movieService.getRecommendMovie(query, limit);
    }

    @PostMapping("search")
    public GeneralResponse<List<Movie>> getMovieSearch(@RequestBody MovieSearchRequest request) {
        return movieService.search(request);
    }

    @GetMapping("/summary")
    public GeneralResponse<Long> getTotalMovie() {
        return movieService.getTotalMovie();
    }

    @PostMapping("/lock")
    public GeneralResponse<Movie> lockMovie(@RequestBody LockMovieRequest request) {
        return movieService.lockMovie(request);
    }

}
