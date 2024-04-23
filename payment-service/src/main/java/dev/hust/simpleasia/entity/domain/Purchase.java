package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchase")
@Getter
@Setter
@ToString
public class Purchase {
    @Id
    @Column(length = 30)
    private String id;
    private String userId;
    private String movieId;
    @Transient
    private Movie movie;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void init() {
        id = ULID.random();
        createdAt = new Date();
    }

    @PostPersist
    public void update() {
        updatedAt = new Date();
    }
}
