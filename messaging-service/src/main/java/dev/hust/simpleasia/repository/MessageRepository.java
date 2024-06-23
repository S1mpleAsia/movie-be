package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = "SELECT m FROM Message m " +
            "where m.senderId = :userId OR m.receiverId = :userId " +
            "GROUP BY GREATEST(m.senderId, m.receiverId), LEAST(m.senderId, m.receiverId) " +
            "ORDER BY MAX(m.createdAt) DESC")
    List<Message> getLatestMessage(@Param("userId") String userId);

    List<Message> findAllBySenderIdOrReceiverIdOrderByCreatedAt(String senderId, String receiverId);
}
