package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Actor;
import dev.hust.simpleasia.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActorService {
    private final ActorRepository actorRepository;

    public Actor findActor(Long id) {
        return actorRepository.findById(id).orElseThrow(() -> new BusinessException("Can not find actor"));
    }

    public GeneralResponse<Actor> findActorDetail(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new BusinessException("Can not find actor"));

        return GeneralResponse.success(actor);
    }
}
