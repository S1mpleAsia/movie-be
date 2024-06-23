package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "movie_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MovieImage {
    @Id
    @Column(length = 30)
    private String id;
    private Long movieId;
    private String imagePath;

    @PrePersist
    public void init() {
        id = ULID.random();
    }
}
