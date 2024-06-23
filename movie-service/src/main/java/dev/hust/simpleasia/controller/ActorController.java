package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.Actor;
import dev.hust.simpleasia.entity.dto.DetailActorInfoResponse;
import dev.hust.simpleasia.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actor")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping("{id}")
    public GeneralResponse<Actor> getActorDetail(@PathVariable("id") Long id) {
        return actorService.findActorDetail(id);
    }

    @GetMapping("/info/{id}")
    public GeneralResponse<DetailActorInfoResponse> getDetailActorInfo(@PathVariable("id") Long id) {
        return actorService.getDetailActorInfo(id);
    }
}
