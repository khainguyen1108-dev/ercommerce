package storemanagement.example.group_15.domain.rules.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
