package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Feedback;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.*;
import dev.hust.simpleasia.port.RestTemplateClient;
import dev.hust.simpleasia.repository.FeedbackRepository;
import dev.hust.simpleasia.repository.MovieRepository;
import dev.hust.simpleasia.utils.helper.CustomStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;
    private final RestTemplateClient restTemplateClient;

    @Transactional
    public GeneralResponse<FeedbackResponse> addFeedback(FeedbackRequest request) {
        log.info("--- Update Movie information ---");
        Movie movie = movieRepository.findById(request.getMovieId()).
                orElseThrow(() -> new BusinessException("Movie not found"));

        // Set vote count
        movie.setVoteCount(movie.getVoteCount() + 1);
        int[] votes = CustomStringUtils.splitStringToIntArray(movie.getVoteOverall(), ",");

        // Set overall
        votes[5 - request.getVote()] = votes[5 - request.getVote()] + 1;
        movie.setVoteOverall(CustomStringUtils.mergeIntArrayToString(votes, ","));

        // Set average
        Double newTotalVote = movie.getVoteAverage() * (movie.getVoteCount() - 1) + request.getVote();
        movie.setVoteAverage(newTotalVote / movie.getVoteCount());
        movieRepository.save(movie);

        log.info("--- End Movie information ---");

        log.info("--- Start Feedback insert ---");
        Feedback feedback = Feedback.builder()
                .vote(request.getVote())
                .feedback(request.getFeedback())
                .userId(request.getUserId())
                .movieId(request.getMovieId())
                .build();

        Feedback persistedFeedback = feedbackRepository.saveAndFlush(feedback);
        log.info("--- End Feedback insert ---");

        GeneralResponse<UserCredential> userCredential = restTemplateClient.get(
                        "http://127.0.0.1:8081/api/auth/detail?id={id}",
                        new ParameterizedTypeReference<GeneralResponse<UserCredential>>() {
                        },
                        null, feedback.getUserId())
                .getBody();

        assert userCredential != null;

        return GeneralResponse.success(
                FeedbackResponse.builder()
                        .id(persistedFeedback.getId())
                        .feedback(persistedFeedback.getFeedback())
                        .vote(request.getVote())
                        .movieId(movie.getId())
                        .userId(request.getUserId())
                        .createdAt(persistedFeedback.getCreatedAt())
                        .updatedAt(persistedFeedback.getUpdatedAt())
                        .userCredential(userCredential.getData())
                        .build()
        );
    }

    public GeneralResponse<FeedbackOverallResponse> getFeedbackOverall(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new BusinessException("Movie not found"));

        int[] votes = CustomStringUtils.splitStringToIntArray(movie.getVoteOverall(), ",");
        List<FeedbackComponent> feedbackComponentList = new ArrayList<>();

        for (int i = 0; i < votes.length; i++) {
            FeedbackComponent feedbackComponent = FeedbackComponent.builder()
                    .rating(5 - i)
                    .total(votes[i])
                    .percentage(movie.getVoteCount() != 0 ? 1.0 * votes[i] / movie.getVoteCount() : 0)
                    .build();

            feedbackComponentList.add(feedbackComponent);
        }

        return GeneralResponse.success(
                FeedbackOverallResponse.builder()
                        .movieId(movie.getId())
                        .voteAverage(movie.getVoteAverage())
                        .voteCount(movie.getVoteCount())
                        .voteOverall(movie.getVoteOverall())
                        .feedbackComponentList(feedbackComponentList)
                        .build()
        );
    }

    @Transactional
    public GeneralResponse<FeedbackDeleteResponse> deleteFeedback(String feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new BusinessException("Can not find feedback"));

        Movie movie = movieRepository.findById(feedback.getMovieId())
                .orElseThrow(() -> new BusinessException("Can not find movie"));

        movie.setVoteCount(movie.getVoteCount() - 1);

        int[] votes = CustomStringUtils.splitStringToIntArray(movie.getVoteOverall(), ",");

        votes[5 - feedback.getVote()] = votes[5 - feedback.getVote()] - 1;
        movie.setVoteOverall(CustomStringUtils.mergeIntArrayToString(votes, ","));

        double newTotalVote = movie.getVoteAverage() * (movie.getVoteCount() + 1) - feedback.getVote();
        movie.setVoteAverage(newTotalVote / movie.getVoteCount());

        movieRepository.save(movie);
        feedbackRepository.delete(feedback);

        return GeneralResponse.success(
                FeedbackDeleteResponse.builder()
                        .status("SUCCESS")
                        .build()
        );
    }

    public GeneralResponse<List<FeedbackResponse>> getFeedbackList(Long movieId, Integer page, Integer size) {
        Page<Feedback> feedbacks = feedbackRepository.findAllByMovieIdOrderByCreatedAtDesc(movieId, PageRequest.of(page, size));

        List<FeedbackResponse> feedbackResponseList = feedbacks.stream().map(feedback -> {
            FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                    .id(feedback.getId())
                    .feedback(feedback.getFeedback())
                    .movieId(movieId)
                    .userId(feedback.getUserId())
                    .vote(feedback.getVote())
                    .createdAt(feedback.getCreatedAt())
                    .updatedAt(feedback.getUpdatedAt())
                    .build();

            GeneralResponse<UserCredential> userCredential = restTemplateClient.get(
                            "http://127.0.0.1:8081/api/auth/detail?id={id}",
                            new ParameterizedTypeReference<GeneralResponse<UserCredential>>() {
                            },
                            null, feedback.getUserId())
                    .getBody();

            if (userCredential != null && userCredential.getStatus().getStatusCode().equals(HttpStatus.SC_OK)) {
                feedbackResponse.setUserCredential(userCredential.getData());
            }

            return feedbackResponse;
        }).collect(Collectors.toList());

        return GeneralResponse.success(feedbackResponseList);
    }

    public GeneralResponse<Integer> getFeedbackTotalPage(Long movieId) {
        List<Feedback> feedbacks = feedbackRepository.findAllByMovieId(movieId);

        Integer page = (int) Math.ceil((double) feedbacks.size() / 3);
        return GeneralResponse.success(page);
    }

    @Transactional
    public GeneralResponse<FeedbackResponse> updateFeedback(FeedbackUpdateRequest request) {
        Feedback feedback = feedbackRepository.findById(request.getId()).orElseThrow(() -> new BusinessException("Can not found your feedback"));


        Movie movie = movieRepository.findById(feedback.getMovieId())
                .orElseThrow(() -> new BusinessException("Can not find movie"));

        int[] votes = CustomStringUtils.splitStringToIntArray(movie.getVoteOverall(), ",");
        votes[5 - feedback.getVote()] = votes[5 - feedback.getVote()] - 1;
        votes[5 - request.getVote()] = votes[5 - request.getVote()] + 1;
        movie.setVoteOverall(CustomStringUtils.mergeIntArrayToString(votes, ","));

        double newTotalVote = movie.getVoteAverage() * movie.getVoteCount() - feedback.getVote() + request.getVote();
        movie.setVoteAverage(newTotalVote / movie.getVoteCount());

        feedback.setFeedback(request.getFeedback());
        feedback.setVote(request.getVote());

        FeedbackResponse response = FeedbackResponse.builder()
                .id(feedback.getId())
                .feedback(feedback.getFeedback())
                .movieId(feedback.getMovieId())
                .userId(feedback.getUserId())
                .vote(feedback.getVote())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .build();
        movieRepository.save(movie);
        feedbackRepository.save(feedback);

        return GeneralResponse.success(response);
    }

    public GeneralResponse<Integer> getFeedbackSummary(String userId) {
        List<Feedback> feedbacks = feedbackRepository.findAllByUserId(userId);

        return GeneralResponse.success(feedbacks.size());
    }
}
