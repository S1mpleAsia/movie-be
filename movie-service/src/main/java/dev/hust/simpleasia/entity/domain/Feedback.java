package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@ToString
public class Feedback {
    @Id
    @Column(length = 30)
    private String id;
    private String userId;
    private Long movieId;
    private String feedback;
    private Integer vote;
    @Transient
    private UserCredential userCredential;

    @PrePersist
    public void init() {
        id = ULID.random();
    }

}
