package dev.hust.simpleasia.entity.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackRequest {
    private Long movieId;
    private String userId;
    private String feedback;
    private Integer vote;
}
