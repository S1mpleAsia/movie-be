package dev.hust.simpleasia.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExistedUserResponse {
    private String email;
    private boolean isExisted;
}
