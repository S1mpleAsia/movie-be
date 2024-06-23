package dev.hust.simpleasia.entity.dto;

import dev.hust.simpleasia.utils.PackageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StripeCheckoutRequest {
    private String email;
    private String name;
    private String userId;
    private PackageType type;
}
