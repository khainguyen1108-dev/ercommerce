package storemanagement.example.group_15.domain.rules.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;

@Entity
@Table(name = "rule_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulePermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_id", nullable = false)
    private RuleEntity rule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", nullable = false)
    private PermissionEntity permission;

    private String access;

}