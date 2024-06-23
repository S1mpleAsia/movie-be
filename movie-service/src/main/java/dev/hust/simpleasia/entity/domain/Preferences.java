package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Preferences {
    @Id
    @Column(length = 30)
    private String id;
    private String userId;
    private Long movieId;
    private Boolean isFavourite;
    @Transient
    private Movie movie;
    @Transient
    private UserCredential userCredential;

    @PrePersist
    public void init() {
        id = ULID.random();
    }
}
