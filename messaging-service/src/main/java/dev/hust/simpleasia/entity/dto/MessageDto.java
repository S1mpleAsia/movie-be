package dev.hust.simpleasia.entity.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDto {
    private String id;
    private String senderId;
    private String receiverId;
    private String title;
    private String content;
    private String imagePath;
    private Boolean isSeen;
    private String type;
    private Date createdAt;
    private Long movieId;
    private MovieDetailResponse movie;
}
