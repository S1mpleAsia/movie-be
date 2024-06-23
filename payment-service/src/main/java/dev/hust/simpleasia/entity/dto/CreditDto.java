package dev.hust.simpleasia.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreditDto {
    private Long creditId;
    private Long actorId;
    private String name;
    private String profilePath;
    private String biography;
    private String birthday;
    private String gender;
    private String role;
    private String casting;
}