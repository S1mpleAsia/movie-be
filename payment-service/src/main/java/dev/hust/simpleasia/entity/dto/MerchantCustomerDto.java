package dev.hust.simpleasia.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantCustomerDto {
    private String id;
    private String email;
    private String userId;
    private String customerId;
    private String merchantName;
    private Date createdAt;
    private Date updatedAt;
}
