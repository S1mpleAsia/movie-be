package dev.hust.simpleasia.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredentialUpdateRequest {
    private String id;
    private String fullName;
    private String birthday;
    private String gender;
    private String region;
}
