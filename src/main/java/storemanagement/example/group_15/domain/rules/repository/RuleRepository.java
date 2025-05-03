package storemanagement.example.group_15.domain.rules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import storemanagement.example.group_15.domain.rules.entity.RuleEntity;

import java.util.Optional;

public interface RuleRepository extends JpaRepository<RuleEntity,Long> {
    Optional<RuleEntity> findByName(String name);
}
