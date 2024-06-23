package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.Feedback;
import dev.hust.simpleasia.entity.domain.Genre;
import dev.hust.simpleasia.entity.domain.MovieImage;
import dev.hust.simpleasia.entity.domain.Video;
import dev.hust.simpleasia.utils.constant.MovieStatusType;
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
public class MovieInitRequest {
    private Long id;
    private String language;
    private String title;
    private List<String> genres;
    private String overview;
    private String runtime;
    private String backdropPath;
    private String posterPath;
    private String releaseDate;
    private String videoPath;
    private MovieStatusType status;
    private Date createdAt;
    private Date updatedAt;
}
