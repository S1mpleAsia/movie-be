package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    List<Movie> findAllByVoteOverallIsNull();

    @Query(
            value = "SELECT m FROM Movie m where m.voteCount > 100 and m.voteAverage > 4 and YEAR(m.releaseDate) > 2010" +
                    " ORDER BY m.voteAverage DESC"
    )
    List<Movie> getTopRatedMovieList(Pageable pageable);

    @Query(value = "SELECT m FROM Movie m where m.genres in :genreIds")
    List<Movie> findWithFilter(@Param("genreIds") List<Long> genreIds, Pageable pageable);
}
