package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.dto.CheckPreferenceResponse;
import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import dev.hust.simpleasia.entity.dto.UserPreferenceRequest;
import dev.hust.simpleasia.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preference")
@RequiredArgsConstructor
public class PreferenceController {
    private final PreferenceService preferenceService;

    @GetMapping
    public GeneralResponse<List<MovieDetailResponse>> getPreferenceList(@RequestParam("userId") String userId) {
        return preferenceService.getPreferenceList(userId);
    }

    @GetMapping("/check")
    public GeneralResponse<CheckPreferenceResponse> checkPreference(@RequestParam("userId") String userId, @RequestParam("movieId") Long movieId) {
        return preferenceService.checkPreference(userId, movieId);
    }


    @PostMapping
    public GeneralResponse<String> togglePreference(@RequestBody UserPreferenceRequest request) {
        return preferenceService.togglePreference(request);
    }

    @GetMapping("/summary")
    public GeneralResponse<Integer> getPreferenceSummary(@RequestParam("userId") String userId) {
        return preferenceService.getPreferenceSummary(userId);
    }
}
