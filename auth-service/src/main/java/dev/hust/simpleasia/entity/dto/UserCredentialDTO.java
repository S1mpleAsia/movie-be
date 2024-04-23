package dev.hust.simpleasia.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentialDTO {
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
