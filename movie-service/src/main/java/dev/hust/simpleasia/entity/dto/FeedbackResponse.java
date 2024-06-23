package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.UserCredential;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedbackResponse {
    private String id;
    private Long movieId;
    private String feedback;
    private Integer vote;
    private String userId;
    private Date createdAt;
    private Date updatedAt;
    private UserCredential userCredential;
}
