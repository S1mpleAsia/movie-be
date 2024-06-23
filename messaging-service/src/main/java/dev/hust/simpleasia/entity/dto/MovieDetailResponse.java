package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.Feedback;
import dev.hust.simpleasia.entity.domain.Genre;
import dev.hust.simpleasia.entity.domain.MovieImage;
import dev.hust.simpleasia.entity.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailResponse {
    private Long id;
    private String language;
    private String title;
    private String genres;
    private String overview;
    private Integer runtime;
    private String backdropPath;
    private String posterPath;
    private Date releaseDate;
    private Integer popularity;
    private Double voteAverage;
    private Integer voteCount;
    private String status;
    private List<MovieImage> imageList;
    private List<Feedback> feedbacks;
    private List<CreditDto> credits;
    private List<Genre> genreList;
    private List<Video> videos;
    private Video trailer;
    private Date createdAt;
    private Date updatedAt;
}