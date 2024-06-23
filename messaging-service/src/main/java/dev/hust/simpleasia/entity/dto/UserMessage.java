package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.UserCredential;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserMessage {
    private String partnerId;
    private UserCredential userCredential;
    private List<MessageDto> messageDtoList;
}
