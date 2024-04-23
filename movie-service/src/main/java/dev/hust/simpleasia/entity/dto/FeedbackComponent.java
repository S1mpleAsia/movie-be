package dev.hust.simpleasia.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedbackComponent {
    private Integer rating;
    private Integer total;
    private Double percentage;
}
