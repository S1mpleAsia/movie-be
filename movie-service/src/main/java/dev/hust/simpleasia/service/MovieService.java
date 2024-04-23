package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.domain.Video;
import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import dev.hust.simpleasia.mapper.MovieMapstruct;
import dev.hust.simpleasia.repository.MovieRepository;
import dev.hust.simpleasia.utils.constant.VideoType;
import dev.hust.simpleasia.utils.helper.MockVoting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieCrewService movieCrewService;
    private final MovieMapstruct movieMapstruct;
    private final MovieImageService movieImageService;
    private final GenreService genreService;
    private final VideoService videoService;

    public GeneralResponse<List<Movie>> getMovieList(Integer page) {
        List<Movie> movieList = movieRepository.findAll(PageRequest.of(page, 10, Sort.by("releaseDate").descending()))
                .get().toList();

        return GeneralResponse.success(movieList);
    }

    public GeneralResponse<MovieDetailResponse> getDetail(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Can not find movie"));

        MovieDetailResponse movieDetailResponse = movieMapstruct.toDetailResponse(movie);
        setCredits(id, movieDetailResponse);
        setGenreList(movieDetailResponse);
        setImageList(id, movieDetailResponse);
        setVideoList(id, movieDetailResponse);

        return GeneralResponse.success(movieDetailResponse);
    }

    private void setVideoList(Long id, MovieDetailResponse response) {
        List<Video> videoList = videoService.getVideoList(id);

        Video trailer = videoList.stream().filter(video -> video.getVideoType().equals(VideoType.Trailer.name())).toList().get(0);
        response.setTrailer(trailer);
        response.setVideos(videoList);
    }

    private void setCredits(Long id, MovieDetailResponse response) {
        response.setCredits(movieCrewService.findAllByMovieId(id));
    }

    private void setGenreList(MovieDetailResponse response) {
        response.setGenreList(genreService.getGenreFromMovie(response.getGenres()));
    }

    private void setImageList(Long id, MovieDetailResponse response) {
        response.setImageList(movieImageService.getImageList(id));
    }

    public void mockFeedbackOverall() {
        List<Movie> movies = movieRepository.findAllByVoteOverallIsNull();

        movies.forEach(movie -> {
            int i = 0;
            while (true) {
                if (i++ > 50) break;
                String mockData = MockVoting.generateVote(movie.getVoteAverage(), movie.getVoteCount(), movie.getVoteCount());
                if (!mockData.equals("Failed")) {
                    movie.setVoteOverall(mockData);
                    movieRepository.save(movie);
                    System.out.printf("Movie with id: [%s] done%n", movie.getId());
                    break;
                }
            }
        });
    }
}
