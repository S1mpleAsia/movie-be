package dev.hust.simpleasia.entity.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackOverallResponse {
    private Long movieId;
    private Double voteAverage;
    private Integer voteCount;
    private String voteOverall;
    private List<FeedbackComponent> feedbackComponentList;
}
