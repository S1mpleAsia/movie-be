package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Genre;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.domain.MovieImage;
import dev.hust.simpleasia.entity.domain.Video;
import dev.hust.simpleasia.entity.dto.*;
import dev.hust.simpleasia.mapper.MovieMapstruct;
import dev.hust.simpleasia.port.RestTemplateClient;
import dev.hust.simpleasia.repository.MovieRepository;
import dev.hust.simpleasia.utils.constant.VideoType;
import dev.hust.simpleasia.utils.helper.DateUtils;
import dev.hust.simpleasia.utils.helper.MockVoting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final RestTemplateClient restTemplateClient;

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

    public GeneralResponse<List<Movie>> getTopRatedMovieList() {
        List<Movie> movieList = movieRepository.getTopRatedMovieList(PageRequest.of(0, 10));

        return GeneralResponse.success(movieList);
    }

    public GeneralResponse<Movie> changeMovieStatus(MovieStatusRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new BusinessException("Can not find movie"));

        movie.setStatus(request.getStatus().name());
        Movie persistedMovie = movieRepository.saveAndFlush(movie);
        return GeneralResponse.success(persistedMovie);
    }

    public GeneralResponse<MovieDetailResponse> createMovie(MovieInitRequest request) {
        List<Genre> genreList = genreService.findAllGenreByNames(request.getGenres());
        String movieGenres = genreList.stream().map(genre -> String.valueOf(genre.getId())).collect(Collectors.joining(","));


        Movie movie = Movie.builder()
                .title(request.getTitle())
                .genres(movieGenres)
                .overview(request.getOverview())
                .popularity(0)
                .posterPath(request.getPosterPath())
                .backdropPath(request.getBackdropPath())
                .releaseDate(DateUtils.formatDate(request.getReleaseDate(), "yyyy-MM-dd"))
                .voteAverage(0.0)
                .voteCount(0)
                .language(request.getLanguage())
                .runtime(Integer.parseInt(request.getRuntime()))
                .status(request.getStatus().name())
                .voteOverall("0, 0, 0, 0, 0")
                .build();

        Movie savedMovie = movieRepository.saveAndFlush(movie);

        MovieImage movieImage = MovieImage.builder()
                .movieId(savedMovie.getId())
                .imagePath(savedMovie.getBackdropPath())
                .build();

        movieImageService.saveImage(movieImage);

        Video video = Video.builder()
                .movieId(savedMovie.getId())
                .official(true)
                .publishedAt(new Date())
                .site("system")
                .videoKey(request.getVideoPath())
                .videoName(null)
                .videoType("Trailer")
                .build();

        videoService.saveVideo(video);

        return getDetail(savedMovie.getId());
    }

    public GeneralResponse<Object> updateMovie(MovieModifiedRequest request) {
        Movie movie = movieRepository.findById(request.getId()).orElseThrow(() -> new BusinessException("Can not found movie"));

        if (request.getTitle() != null) movie.setTitle(request.getTitle());
        if (request.getOverview() != null) movie.setOverview(request.getOverview());
        if (request.getStatus() != null) movie.setStatus(request.getStatus());
        if (request.getRuntime() != null) movie.setRuntime(request.getRuntime());
        if (request.getGenres() != null) {
            List<Genre> genres = genreService.findAllGenreByNames(request.getGenres());
            String movieGenres = genres.stream().map(genre -> String.valueOf(genre.getId())).collect(Collectors.joining(","));
            movie.setGenres(movieGenres);
        }

        if (request.getLanguage() != null) movie.setLanguage(request.getLanguage());
        if (request.getReleaseDate() != null) movie.setReleaseDate(request.getReleaseDate());
        if (request.getPosterPath() != null) movie.setPosterPath(request.getPosterPath());
        if (request.getBackdropPath() != null) movie.setBackdropPath(request.getBackdropPath());
        if (request.getVideoPath() != null) {
            List<Video> videoList = videoService.getVideoList(request.getId());

            if (videoList.isEmpty()) {
                Video video = Video.builder()
                        .videoKey(request.getVideoPath())
                        .movieId(request.getId())
                        .videoName(null)
                        .videoType("Trailer")
                        .site("system")
                        .official(true)
                        .publishedAt(new Date())
                        .build();
                videoService.saveVideo(video);
            } else {
                Video video = videoList.get(0);
                video.setVideoKey(request.getVideoPath());
                video.setSite("system");
                videoService.saveVideo(video);
            }
        }

        movieRepository.save(movie);

        return GeneralResponse.success(null);
    }

    public GeneralResponse<List<Movie>> getAllMovie() {
        List<Movie> movies = movieRepository.findAll();
        return GeneralResponse.success(movies);
    }

    public GeneralResponse<List<Movie>> getRecommendMovie(String query, Integer limit) {
        List<RecommendQueryResponse> response = restTemplateClient.get("http://127.0.0.1:5000/recommend?query={query}&limit={limit}",
                        new ParameterizedTypeReference<List<RecommendQueryResponse>>() {
                        }, null, query, limit)
                .getBody();


        assert response != null;
        List<Long> movieIdList = response.stream().map(RecommendQueryResponse::getMovieId).toList();
        List<Movie> movieList = movieRepository.findAllById(movieIdList);

        return GeneralResponse.success(movieList);
    }

    public GeneralResponse<List<Movie>> search(MovieSearchRequest request) {
        String[] sortParams = request.getOrderValue().split("\\.");
        Pageable pageable = PageRequest.of(0, request.getLimit(), Sort.by(
                Sort.Direction.fromString(sortParams[1]),
                sortParams[0])
        );


        if (request.getQuery() == null || request.getQuery().isEmpty()) {
            if (request.getFilter() == null || request.getFilter().isEmpty())
                return GeneralResponse.success(movieRepository.findAll(pageable).toList());

            return GeneralResponse.success(movieRepository.findWithFilter(request.getFilter(), pageable));
        }

        List<SearchQueryResponse> response = restTemplateClient.get("http://127.0.0.1:5000/search?query={query}&limit={limit}&genres={genres}",
                        new ParameterizedTypeReference<List<SearchQueryResponse>>() {
                        }, null, request.getQuery(), request.getLimit(), request.getFilter())
                .getBody();

        assert response != null;
        List<Long> movieIdList = response.stream().map(SearchQueryResponse::getMovieId).toList();
        List<Movie> movieList = movieRepository.findAllById(movieIdList);

        return GeneralResponse.success(reorderMovies(request.getOrderValue(), movieList));
    }

    private List<Movie> reorderMovies(String sortOrder, List<Movie> movieList) {
        switch (sortOrder) {
            case "popularity.desc":
                movieList.sort(Comparator.comparing(Movie::getPopularity).reversed());
                break;
            case "popularity.asc":
                movieList.sort(Comparator.comparing(Movie::getPopularity));
                break;
            case "title.asc":
                movieList.sort(Comparator.comparing(Movie::getTitle));
                break;
            case "vote_average.asc":
                movieList.sort(Comparator.comparing(Movie::getVoteAverage));
                break;
            case "vote_average.desc":
                movieList.sort(Comparator.comparing(Movie::getVoteAverage).reversed());
                break;
            case "release_date.asc":
                movieList.sort(Comparator.comparing(Movie::getReleaseDate));
                break;
            case "release_date.desc":
                movieList.sort(Comparator.comparing(Movie::getReleaseDate).reversed());
            default:
                // Handle default case or throw exception if needed
                break;
        }
        return movieList;
    }

    public GeneralResponse<Long> getTotalMovie() {
        long count = movieRepository.count();
        return GeneralResponse.success(count);
    }

    public GeneralResponse<MovieModifiedResponse> getBasicInfo(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new BusinessException("Can not found movie"));
        List<Video> videoList = videoService.getVideoList(id);
        List<Genre> genreList = genreService.getGenreFromMovie(movie.getGenres());
        List<String> genreNames = genreList.stream().map(Genre::getName).collect(Collectors.toList());


        return GeneralResponse.success(
                MovieModifiedResponse.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .overview(movie.getOverview())
                        .runtime(movie.getRuntime())
                        .genres(genreNames)
                        .releaseDate(movie.getReleaseDate())
                        .posterPath(movie.getPosterPath())
                        .backdropPath(movie.getBackdropPath())
                        .videoPath(videoList.isEmpty() ? null : videoList.get(0).getVideoKey())
                        .videoSite(videoList.isEmpty() ? null : videoList.get(0).getSite())
                        .createdAt(movie.getCreatedAt())
                        .updatedAt(movie.getUpdatedAt())
                        .build()
        );
    }

    public GeneralResponse<Movie> lockMovie(LockMovieRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new BusinessException("Can not found movie"));

        if (Objects.equals(request.getStatus(), "Released")) {
            movie.setStatus("Closed");
        } else movie.setStatus("Released");

        Movie savedMovie = movieRepository.saveAndFlush(movie);
        return GeneralResponse.success(savedMovie);
    }
}
