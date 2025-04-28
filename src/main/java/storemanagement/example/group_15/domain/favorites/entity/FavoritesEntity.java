package storemanagement.example.group_15.domain.favorites.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "favorites_entity") // 👉 bạn đổi "your_table_name" thành tên bảng thực tế nhé
@Data
public class FavoritesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;
}




