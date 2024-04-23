package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.Genre;
import dev.hust.simpleasia.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genre")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public GeneralResponse<List<Genre>> getAllGenres() {
        return genreService.getAllGenres();
    }
}
