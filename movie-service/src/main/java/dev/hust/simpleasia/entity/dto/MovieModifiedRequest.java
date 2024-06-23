package dev.hust.simpleasia.entity.dto;

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
public class MovieModifiedRequest {
    private Long id;
    private String title;
    private String overview;
    private Integer runtime;
    private List<String> genres;
    private String language;
    private String status;
    private Date releaseDate;
    private String posterPath;
    private String backdropPath;
    private String videoPath;
    private String videoSite;
}
