package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.Message;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.MessageDto;
import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import dev.hust.simpleasia.entity.dto.UserMessage;
import dev.hust.simpleasia.mapper.MessageMapstruct;
import dev.hust.simpleasia.port.RestTemplateClient;
import dev.hust.simpleasia.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapstruct messageMapstruct;
    private final RestTemplateClient restTemplateClient;

    @Value("${app.internal.ip}")
    private String internalIp;

    public GeneralResponse<List<MessageDto>> getMessages() {
        List<Message> messageList = messageRepository.findAll();
        getMovieLink(messageList);

        return GeneralResponse.success(messageMapstruct.toDtoList(messageList));
    }

    public GeneralResponse<MessageDto> saveMessage(MessageDto messageDto) {
        Message message = messageMapstruct.toEntity(messageDto);
        Message persistMessage = messageRepository.saveAndFlush(message);
        messageDto.setId(persistMessage.getId());

        return GeneralResponse.success(messageDto);
    }

    public GeneralResponse<List<MessageDto>> getLatestMessage(String userId) {
        List<Message> messageList = messageRepository.getLatestMessage(userId);

        return GeneralResponse.success(messageMapstruct.toDtoList(messageList));
    }

    public GeneralResponse<List<UserMessage>> getUserMessages(String userId) {

        Map<String, List<Message>> separateMessages = separateMessages(userId);
        List<UserMessage> userMessageList = new ArrayList<>();

        for (Map.Entry<String, List<Message>> entry : separateMessages.entrySet()) {
            String partnerId = entry.getKey();
            List<Message> messageList = entry.getValue();

            GeneralResponse<UserCredential> response = restTemplateClient
                    .get("http://" + internalIp + ":8081/api/auth/detail?id={id}",
                            new ParameterizedTypeReference<GeneralResponse<UserCredential>>() {
                            },
                            null,
                            partnerId).getBody();

            assert response != null;

            getMovieLink(messageList);

            UserMessage userMessage = UserMessage.builder()
                    .partnerId(partnerId)
                    .userCredential(response.getData())
                    .messageDtoList(messageMapstruct.toDtoList(messageList))
                    .build();

            userMessageList.add(userMessage);
        }

        return GeneralResponse.success(userMessageList);
    }

    private void getMovieLink(List<Message> messageList) {
        messageList.forEach(message -> {
            if (message.getMovieId() != null) {
                GeneralResponse<MovieDetailResponse> generalResponse = restTemplateClient
                        .get("http://" + internalIp + ":8082/api/movie/{id}",
                                new ParameterizedTypeReference<GeneralResponse<MovieDetailResponse>>() {
                                }, null, message.getMovieId())
                        .getBody();

                assert generalResponse != null;

                if (generalResponse.getStatus().getStatusCode().equals(HttpStatus.SC_OK)) {
                    message.setMovie(generalResponse.getData());
                }
            }
        });
    }


    private Map<String, List<Message>> separateMessages(String userId) {
        List<Message> messages = messageRepository.findAllBySenderIdOrReceiverIdOrderByCreatedAt(userId, userId);

        Map<String, List<Message>> separateMessages = new HashMap<>();

        for (Message message : messages) {
            if (!message.getSenderId().equals(userId) && !separateMessages.containsKey(message.getSenderId())) {
                separateMessages.put(message.getSenderId(), new ArrayList<>());
            }

            if (!message.getReceiverId().equals(userId) && !separateMessages.containsKey(message.getReceiverId())) {
                separateMessages.put(message.getReceiverId(), new ArrayList<>());
            }

            if (message.getSenderId().equals(userId)) {
                separateMessages.get(message.getReceiverId()).add(message);
            } else if (message.getReceiverId().equals(userId)) {
                separateMessages.get(message.getSenderId()).add(message);
            }

        }

        return separateMessages;
    }
}
