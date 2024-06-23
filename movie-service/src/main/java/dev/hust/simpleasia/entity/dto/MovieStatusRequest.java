package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.utils.constant.MovieStatusType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieStatusRequest {
    private Long movieId;
    private MovieStatusType status;
}
