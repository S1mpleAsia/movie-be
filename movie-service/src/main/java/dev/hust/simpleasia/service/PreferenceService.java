package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.domain.Preferences;
import dev.hust.simpleasia.entity.dto.CheckPreferenceResponse;
import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import dev.hust.simpleasia.entity.dto.UserPreferenceRequest;
import dev.hust.simpleasia.mapper.MovieMapstruct;
import dev.hust.simpleasia.repository.MovieRepository;
import dev.hust.simpleasia.repository.PreferencesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreferenceService {
    private final PreferencesRepository preferencesRepository;
    private final MovieRepository movieRepository;
    private final MovieMapstruct movieMapstruct;

    public GeneralResponse<CheckPreferenceResponse> checkPreference(String userId, Long movieId) {
        Optional<Preferences> preferencesOptional = preferencesRepository.findFirstByUserIdAndMovieId(userId, movieId);

        CheckPreferenceResponse checkResponse = CheckPreferenceResponse.builder()
                .isFavourite(false)
                .build();

        if (preferencesOptional.isPresent()) {
            checkResponse.setIsFavourite(preferencesOptional.get().getIsFavourite());
            return GeneralResponse.success(checkResponse);
        }

        return GeneralResponse.success(checkResponse);
    }

    public GeneralResponse<List<MovieDetailResponse>> getPreferenceList(String userId) {
        List<Preferences> preferencesList = preferencesRepository.getAllByUserId(userId);

        List<MovieDetailResponse> movieResponse = preferencesList.stream().map(preferences -> {
            Movie movie = movieRepository.findById(preferences.getMovieId()).orElseThrow(() -> new BusinessException("Can not find movie"));

            return movieMapstruct.toDetailResponse(movie);
        }).toList();


        return GeneralResponse.success(movieResponse);
    }

    public GeneralResponse<String> togglePreference(UserPreferenceRequest request) {
        Preferences preferences = preferencesRepository
                .findFirstByUserIdAndMovieId(request.getUserId(), request.getMovieId())
                .orElse(Preferences.builder()
                        .userId(request.getUserId())
                        .movieId(request.getMovieId())
                        .isFavourite(false)
                        .build());

        preferences.setIsFavourite(!preferences.getIsFavourite());
        preferencesRepository.save(preferences);

        return GeneralResponse.success("Update successfully");
    }

    public GeneralResponse<Integer> getPreferenceSummary(String userId) {
        List<Preferences> preferencesList = preferencesRepository.findAllByUserIdAndIsFavourite(userId, true);

        return GeneralResponse.success(preferencesList.size());
    }
}
