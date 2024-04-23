package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.UserCredential;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackResponse {
    private String id;
    private Long movieId;
    private String feedback;
    private Integer vote;
    private String userId;
    private UserCredential userCredential;
}
