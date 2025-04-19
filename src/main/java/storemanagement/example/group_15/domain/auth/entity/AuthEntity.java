package storemanagement.example.group_15.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.auth.constant.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Lưu dưới dạng chuỗi: "ADMIN", "USER"
    private Role role; // ví dụ: ADMIN, USER

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

    public AuthEntity(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    public AuthEntity(){

    }
}
