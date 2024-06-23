package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreferencesRepository extends JpaRepository<Preferences, String> {
    Optional<Preferences> findFirstByUserIdAndMovieId(String userId, Long movieId);

    List<Preferences> getAllByUserId(String userId);
    List<Preferences> findAllByUserIdAndIsFavourite(String userId, Boolean isFavourite);
}
