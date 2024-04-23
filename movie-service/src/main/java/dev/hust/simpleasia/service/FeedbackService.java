package dev.hust.simpleasia.service;

import dev.hust.simpleasia.repository.FeedbackRepository;
import dev.hust.simpleasia.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;

}
