package dev.hust.simpleasia.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieActorDto {
    private String id;
    private String name;
    private String imagePath;
    private String gender;
    private String role;
}
