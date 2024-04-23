package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "movie_image")
@Getter
@Setter
@ToString
public class MovieImage {
    @Id
    @Column(length = 30)
    private String id;
    private String movieId;
    private String imagePath;

    @PrePersist
    public void init() {
        id = ULID.random();
    }
}
