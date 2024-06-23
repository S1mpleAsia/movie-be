package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.entity.domain.Purchase;
import dev.hust.simpleasia.entity.domain.UserCredential;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentResponse {
    private String userId;
    private UserCredential userCredential;
    private Purchase purchase;
}
