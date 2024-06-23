package dev.hust.simpleasia.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LockMovieRequest {
    private Long movieId;
    private String status;
}
