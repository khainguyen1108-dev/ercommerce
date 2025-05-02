package storemanagement.example.group_15.domain.collections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import storemanagement.example.group_15.domain.collections.entity.CollectionEntity;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {
}
