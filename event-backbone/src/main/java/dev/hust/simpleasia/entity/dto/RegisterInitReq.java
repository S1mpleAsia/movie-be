package dev.hust.simpleasia.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RegisterInitReq extends BaseEvent {
    private String email;
    private Integer age;
    private String locate;
    private String password;
    private String fullName;
    private String imagePath;
    private String backgroundPath;
    private Date createdAt;
    private Date updatedAt;
}
