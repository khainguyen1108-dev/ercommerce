package storemanagement.example.group_15.domain.permissions.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false, length = 20)
    private PermissionType permission;

    @Column(name = "rules_id", nullable = false)
    private Long rulesId;

    public enum PermissionType {
        CREATE,
        UPDATE,
        DELETE,
        GET
    }
}
