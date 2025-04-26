package storemanagement.example.group_15.domain.vouchers.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "discount_value", nullable = false)
    private Integer discountValue; // Giảm giá % (giá trị int)

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, length = 50)
    private String type; // ALL | PRODUCT | COLLECTION | EVENT

    @Column(columnDefinition = "TEXT")
    private String condition;
}
