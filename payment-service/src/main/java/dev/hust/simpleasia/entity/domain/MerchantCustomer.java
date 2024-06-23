package dev.hust.simpleasia.entity.domain;

import io.azam.ulidj.ULID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "merchant_customer")
@Getter
@Setter
@ToString
public class MerchantCustomer {
    @Id
    @Column(length = 30)
    private String id;
    private String email;
    private String userId;
    private String customerId;
    private String merchantName;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void init() {
        id = ULID.random();
        createdAt = new Date();
    }

    @PostPersist
    public void update() {
        updatedAt = new Date();
    }
}
