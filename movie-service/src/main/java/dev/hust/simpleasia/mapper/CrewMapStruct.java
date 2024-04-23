package dev.hust.simpleasia.mapper;

import dev.hust.simpleasia.entity.domain.Actor;
import dev.hust.simpleasia.entity.domain.Credit;
import dev.hust.simpleasia.entity.dto.CreditDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CrewMapStruct {
    @Mapping(source = "actor.actorId", target = "actorId")
    @Mapping(source = "crew.creditId", target = "creditId")
    @Mapping(source = "actor.name", target = "name")
    @Mapping(source = "actor.profilePath", target = "profilePath")
    @Mapping(source = "actor.biography", target = "biography")
    @Mapping(source = "actor.birthday", target = "birthday")
    @Mapping(source = "actor.gender", target = "gender")
    @Mapping(source = "crew.role", target = "role")
    @Mapping(source = "crew.casting", target = "casting")
    CreditDto from(Actor actor, Credit crew);
}
