package storemanagement.example.group_15.domain.permissions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
