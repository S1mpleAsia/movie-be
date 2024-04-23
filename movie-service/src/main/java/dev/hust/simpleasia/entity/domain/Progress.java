package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "progress")
@Getter
@Setter
@ToString
public class Progress {
    @Id
    @Column(length = 30)
    private String id;
    private String userId;
    private String movieId;
    private Double progress;
    private Long timestop;

    @Transient
    private UserCredential userCredential;

    @PrePersist
    public void init() {
        id = ULID.random();
    }
}
