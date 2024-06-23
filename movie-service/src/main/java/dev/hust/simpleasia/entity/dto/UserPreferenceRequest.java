package dev.hust.simpleasia.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserPreferenceRequest {
    private String userId;
    private Long movieId;
}
