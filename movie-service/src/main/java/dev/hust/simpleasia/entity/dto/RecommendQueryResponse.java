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
public class RecommendQueryResponse {
    @JsonProperty("_additional")
    private AdditionalInfo additionalInfo;

    @JsonProperty("corpus")
    private String corpus;

    @JsonProperty("genre_names")
    private List<String> genreList;

    @JsonProperty("movie_id")
    private Long movieId;

    @JsonProperty("title")
    private String title;

    @Getter
    @Setter
    private static class AdditionalInfo {
        private String explainScore;
        private Double score;
    }
}
