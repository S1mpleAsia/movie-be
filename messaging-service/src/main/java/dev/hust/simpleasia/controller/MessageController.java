package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.dto.MessageDto;
import dev.hust.simpleasia.entity.dto.UserMessage;
import dev.hust.simpleasia.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public GeneralResponse<MessageDto> save(@RequestBody MessageDto messageDto) {
        return messageService.saveMessage(messageDto);
    }

    @GetMapping
    public GeneralResponse<List<MessageDto>> getMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/all")
    public GeneralResponse<List<UserMessage>> getUserMessages(@RequestParam("userId") String userId) {
        return messageService.getUserMessages(userId);
    }

    @GetMapping("/latest")
    public GeneralResponse<List<MessageDto>> getLatestMessage(@RequestParam("userId") String userId) {
        return messageService.getLatestMessage(userId);
    }
}
