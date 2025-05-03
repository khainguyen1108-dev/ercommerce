package storemanagement.example.group_15.domain.permissions.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.rules.entity.RulePermissionEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "permissions",  uniqueConstraints = @UniqueConstraint(columnNames = "urlPattern")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;
    private String urlPattern;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RulePermissionEntity> rulePermissions = new ArrayList<>();
}