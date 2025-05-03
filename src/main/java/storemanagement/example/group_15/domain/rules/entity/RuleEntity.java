package storemanagement.example.group_15.domain.rules.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RulePermissionEntity> rulePermissions = new ArrayList<>();
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<AuthEntity> users = new ArrayList<>();
}