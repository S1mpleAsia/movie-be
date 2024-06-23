package dev.hust.simpleasia.service;

import dev.hust.simpleasia.entity.domain.Actor;
import dev.hust.simpleasia.entity.domain.Credit;
import dev.hust.simpleasia.entity.dto.CreditDto;
import dev.hust.simpleasia.mapper.CrewMapStruct;
import dev.hust.simpleasia.repository.MovieCrewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieCrewService {
    private final MovieCrewRepository movieCrewRepository;
    private final ActorService actorService;
    private final CrewMapStruct crewMapStruct;

    public List<CreditDto> findAllByMovieId(Long movieId) {
        List<Credit> credits = movieCrewRepository.findAllByMovieId(movieId);

        return credits
                .stream()
                .map(credit -> {
                    Actor actor = actorService.findActor(credit.getActorId());
                    return crewMapStruct.from(actor, credit);
                }).toList();
    }

    public void saveAllCredit(List<Credit> creditList) {
        movieCrewRepository.saveAll(creditList);
    }
}
