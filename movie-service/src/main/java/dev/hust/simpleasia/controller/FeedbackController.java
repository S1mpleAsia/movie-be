package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.dto.FeedbackDeleteResponse;
import dev.hust.simpleasia.entity.dto.FeedbackOverallResponse;
import dev.hust.simpleasia.entity.dto.FeedbackRequest;
import dev.hust.simpleasia.entity.dto.FeedbackResponse;
import dev.hust.simpleasia.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/private")
    public GeneralResponse<FeedbackResponse> addFeedback(@RequestBody FeedbackRequest request) {
        return feedbackService.addFeedback(request);
    }

    @GetMapping("/public/overview")
    public GeneralResponse<FeedbackOverallResponse> getFeedbackOverall(@RequestParam("movieId") Long movieId) {
        return feedbackService.getFeedbackOverall(movieId);
    }

    @DeleteMapping("/private")
    public GeneralResponse<FeedbackDeleteResponse> deleteFeedback(@RequestParam("feedbackId") String feedbackId) {
        return feedbackService.deleteFeedback(feedbackId);
    }

    @GetMapping("/public")
    public GeneralResponse<FeedbackResponse> getFeedbackList(@RequestParam("movieId") Long movieId,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "4") Integer size) {
        return feedbackService.getFeedbackList(movieId, page, size);
    }
}
