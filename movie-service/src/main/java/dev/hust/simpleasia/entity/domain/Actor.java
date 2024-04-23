package dev.hust.simpleasia.entity.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "actor")
@Getter
@Setter
@ToString
public class Actor {
    @Id
    @GeneratedValue
    private Long actorId;
    private String name;
    private String profilePath;
    @Column(columnDefinition = "TEXT")
    private String biography;
    private String birthday;
    private String gender;

//    @PrePersist
//    public void init() {
//        id = ULID.random();
//    }
}
