package dev.hust.simpleasia.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SearchQueryResponse {
    @JsonProperty("_additional")
    private AdditionalInfo additionalInfo;

    @JsonProperty("corpus")
    private String corpus;

    @JsonProperty("genre_names")
    private List<String> genreList;

    @JsonProperty("movie_id")
    private Long movieId;

    @JsonProperty("popularity")
    private Integer popularity;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("released_date")
    private String releasedDate;

    @JsonProperty("title")
    private String title;

    @Getter
    @Setter
    private static class AdditionalInfo {
        private String explainScore;
        private Double score;
    }
}
