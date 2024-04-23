package dev.hust.simpleasia.entity.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "movie_credit")
@Getter
@Setter
@ToString
public class Credit {
    @Id
    @GeneratedValue
    private Long creditId;
    private Long movieId;
    private String casting;
    private Long actorId;
    private String role;
}
