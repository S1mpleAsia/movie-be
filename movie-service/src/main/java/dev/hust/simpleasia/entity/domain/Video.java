package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Video {
    @Id
    @Column(length = 30)
    private String videoId;
    private String videoKey;
    private Long movieId;
    private String videoName;
    private Boolean official;
    private Date publishedAt;
    private String site;
    private String videoType;

    @PrePersist
    public void init() {
        videoId = ULID.random();
    }
}
