package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Actor;
import dev.hust.simpleasia.entity.domain.Credit;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.dto.DetailActorInfoResponse;
import dev.hust.simpleasia.mapper.MovieMapstruct;
import dev.hust.simpleasia.repository.ActorRepository;
import dev.hust.simpleasia.repository.MovieCrewRepository;
import dev.hust.simpleasia.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieCrewRepository movieCrewRepository;
    private final MovieRepository movieRepository;
    private final MovieMapstruct movieMapstruct;

    public Actor findActor(Long id) {
        return actorRepository.findById(id).orElseThrow(() -> new BusinessException("Can not find actor"));
    }

    public GeneralResponse<Actor> findActorDetail(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new BusinessException("Can not find actor"));

        return GeneralResponse.success(actor);
    }

    public GeneralResponse<DetailActorInfoResponse> getDetailActorInfo(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new BusinessException("Can not find actor"));
        List<Credit> credits = movieCrewRepository.findAllByActorId(actor.getActorId());

        List<Movie> movieList = credits.stream()
                .map(credit -> movieRepository.findById(credit.getMovieId())
                        .orElseThrow(() -> new BusinessException("Can not find movie")))
                .toList();

        DetailActorInfoResponse payload = DetailActorInfoResponse.builder()
                .actor(actor)
                .movies(movieMapstruct.toDetailResponseList(movieList))
                .build();

        return GeneralResponse.success(payload);
    }
}
