package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Feedback;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.dto.*;
import dev.hust.simpleasia.repository.FeedbackRepository;
import dev.hust.simpleasia.repository.MovieRepository;
import dev.hust.simpleasia.utils.helper.CustomStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;

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

        return GeneralResponse.success(
                FeedbackResponse.builder()
                        .id(persistedFeedback.getId())
                        .feedback(persistedFeedback.getFeedback())
                        .vote(request.getVote())
                        .movieId(movie.getId())
                        .userId(request.getUserId())
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

    public GeneralResponse<FeedbackResponse> getFeedbackList(Long movieId, Integer page, Integer size) {

        return null;
    }
}
