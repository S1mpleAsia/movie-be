package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, String> {
    List<Genre> findByIdIn(List<Long> ids);
}
