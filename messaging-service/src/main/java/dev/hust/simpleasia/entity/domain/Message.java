package dev.hust.simpleasia.entity.domain;

import dev.hust.simpleasia.entity.dto.MovieDetailResponse;
import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
@Getter
@Setter
@ToString
public class Message {
    @Id
    @Column(length = 30)
    private String id;
    private String senderId;
    private String receiverId;
    private String content;
    private String imagePath;
    private Boolean isSeen;
    private String type;
    private Date createdAt;
    private Long movieId;
    @Transient
    private MovieDetailResponse movie;

    @PrePersist
    public void init() {
        id = ULID.random();
        createdAt = new Date();
    }
}
