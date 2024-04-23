package dev.hust.simpleasia.entity.domain;

import dev.hust.simpleasia.entity.dto.MovieActorDto;
import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "movie")
@Getter
@Setter
@ToString
public class Movie {
    @Id
    @Column(length = 30)
    private String id;
    private String language;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private Long runtime;
    private String backdropPath;
    private String posterPath;
    private Date releaseDate;
    private String trailerPath;
    private Integer popularity;
    private String voteOverall;
    @Transient
    private List<MovieImage> imageList;
    @Transient
    private List<MovieActorDto> actors;
    @Transient
    private List<Feedback> feedbacks;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void init() {
        id = ULID.random();
        createdAt = new Date();
    }

    @PostPersist
    public void update() {
        updatedAt = new Date();
    }
}
