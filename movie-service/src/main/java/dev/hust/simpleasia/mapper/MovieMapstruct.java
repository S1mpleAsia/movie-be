package dev.hust.simpleasia.mapper;

import dev.hust.simpleasia.entity.domain.Movie;
import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapstruct {
    MovieDetailResponse toDetailResponse(Movie movie);
}
