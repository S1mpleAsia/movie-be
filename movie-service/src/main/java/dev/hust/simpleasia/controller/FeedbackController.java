package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.entity.domain.Feedback;
import dev.hust.simpleasia.entity.dto.FeedbackRequest;
import dev.hust.simpleasia.entity.dto.FeedbackResponse;
import dev.hust.simpleasia.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public FeedbackResponse addFeedback(@RequestBody FeedbackRequest request) {
        return null;
    }
}
