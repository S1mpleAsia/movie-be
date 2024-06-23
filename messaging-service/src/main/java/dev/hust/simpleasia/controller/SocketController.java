package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.dto.MessageDto;
import dev.hust.simpleasia.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class SocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageService messageService;

    // app/private
    @MessageMapping("/private")
    public MessageDto receivePrivateMessage(@Payload MessageDto messageDto) {
        log.info("Message received: {}", messageDto);
        GeneralResponse<MessageDto> messageDtoGeneralResponse = messageService.saveMessage(messageDto);

        if(!messageDtoGeneralResponse.getStatus().getStatusCode().equals(HttpStatus.SC_OK)) {
            throw new BusinessException("Saved failed");
        }

        // user/[userId]/private
        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiverId(), "/private", messageDtoGeneralResponse.getData());
        simpMessagingTemplate.convertAndSendToUser(messageDto.getSenderId(), "/private", messageDtoGeneralResponse.getData());

        return messageDto;
    }
}
