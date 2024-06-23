package dev.hust.simpleasia.mapper;

import dev.hust.simpleasia.entity.domain.Message;
import dev.hust.simpleasia.entity.dto.MessageDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapstruct {
    Message toEntity(MessageDto messageDto);
    List<MessageDto> toDtoList(List<Message> messages);
}
