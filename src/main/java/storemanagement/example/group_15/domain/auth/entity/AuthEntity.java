package storemanagement.example.group_15.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "role_id", nullable = false)
    private Long roleId; // Sửa thành role_id thay vì Enum nếu theo Excel

    @Column(nullable = false, length = 50)
    private String name; // Chỉnh lại đúng theo file Excel (username -> name)

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDateTime dob; // Ngày sinh, kiểu ngày giờ

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column
    private Boolean isBuy; // Trạng thái đã mua hay chưa

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
}

