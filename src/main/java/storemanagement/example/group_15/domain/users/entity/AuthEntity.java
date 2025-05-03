package storemanagement.example.group_15.domain.users.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import storemanagement.example.group_15.domain.favorites.entity.FavoritesEntity;
import storemanagement.example.group_15.domain.orders.entity.OrderEntity;
import storemanagement.example.group_15.domain.rating.entity.RatingEntity;
import storemanagement.example.group_15.domain.rules.entity.RuleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RuleEntity role;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDateTime dob;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column
    private Boolean isBuy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<FavoritesEntity> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<OrderEntity> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<RatingEntity> ratings = new ArrayList<>();
}
