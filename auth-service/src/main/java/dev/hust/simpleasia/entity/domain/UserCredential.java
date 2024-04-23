package dev.hust.simpleasia.entity.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_credential")
@Getter
@Setter
@ToString
public class UserCredential {
    @Id
    @Column(length = 30)
    private String id;
    private String email;
    private Date birthday;
    private String gender;
    private String password;
    private String fullName;
    private String imagePath;
    private String backgroundPath;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String role;

    @PrePersist
    public void init() {
        role = "USER";
        createdAt = new Date();
    }

    @PostPersist
    public void update() {
        updatedAt = new Date();
    }
}
