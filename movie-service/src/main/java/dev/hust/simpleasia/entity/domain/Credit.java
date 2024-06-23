package dev.hust.simpleasia.entity.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "movie_credit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
