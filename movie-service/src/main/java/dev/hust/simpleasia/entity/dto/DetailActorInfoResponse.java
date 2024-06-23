package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.Actor;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DetailActorInfoResponse {
    private Actor actor;
    private List<MovieDetailResponse> movies;
}
