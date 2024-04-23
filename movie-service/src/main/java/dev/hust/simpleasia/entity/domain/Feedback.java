package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void init() {
        id = ULID.random();
        createdAt = new Date();
    }

    @PostPersist
    public void postPersist() {
        updatedAt = new Date();
    }

}
