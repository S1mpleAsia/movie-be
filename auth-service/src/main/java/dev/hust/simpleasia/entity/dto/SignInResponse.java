package dev.hust.simpleasia.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private String id;
    private String email;
    private Date birthday;
    private String gender;
    @JsonIgnore
    private String password;
    private String fullName;
    private String imagePath;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String token;
    private String role;
    private String region;
}
