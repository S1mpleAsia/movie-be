package dev.hust.simpleasia.entity.domain;

import dev.hust.simpleasia.entity.dto.CreditDto;
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
    @GeneratedValue
    private Long id;
    private String language;
    private String title;
    private String genres;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private Integer runtime;
    private String backdropPath;
    private String posterPath;
    private Date releaseDate;
    private Integer popularity;
    private Double voteAverage;
    private Integer voteCount;
    private String status;
    private String voteOverall;
    @Transient
    private List<MovieImage> imageList;
    @Transient
    private List<CreditDto> credits;
    @Transient
    private List<Feedback> feedbacks;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void init() {
        createdAt = new Date();
    }

    @PostPersist
    public void update() {
        updatedAt = new Date();
    }
}
