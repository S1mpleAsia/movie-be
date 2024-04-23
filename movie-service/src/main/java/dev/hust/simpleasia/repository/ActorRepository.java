package dev.hust.simpleasia.repository;

import dev.hust.simpleasia.entity.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {

}
