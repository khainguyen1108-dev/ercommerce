package storemanagement.example.group_15.domain.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import storemanagement.example.group_15.domain.events.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

}
