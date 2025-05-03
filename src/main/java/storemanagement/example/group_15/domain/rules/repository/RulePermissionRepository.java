package storemanagement.example.group_15.domain.rules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import storemanagement.example.group_15.domain.rules.entity.RulePermissionEntity;

public interface RulePermissionRepository extends JpaRepository<RulePermissionEntity, Long> {
}
