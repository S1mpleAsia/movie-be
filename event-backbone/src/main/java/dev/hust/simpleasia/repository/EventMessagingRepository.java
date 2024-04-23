package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.EventMessaging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventMessagingRepository extends JpaRepository<EventMessaging, String> {
}
