package dev.hust.simpleasia.entity.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event_message_his")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMessaging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(columnDefinition = "TEXT")
    private String requestBody;
    private String eventType;

    @PrePersist
    public void initTime() {
        timestamp = new Date();
    }
}
