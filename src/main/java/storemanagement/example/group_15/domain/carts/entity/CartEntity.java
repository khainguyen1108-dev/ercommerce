package storemanagement.example.group_15.domain.carts.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.products.entity.ProductInCartEntity;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Giá trị đơn hàng
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    // Giá trị cần thanh toán (sau khi áp dụng voucher)
    @Column(name = "total_payment", nullable = false)
    private BigDecimal totalPayment;

    // Mối quan hệ với bảng voucher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private VoucherEntity voucher;

    // Phương thức thanh toán: COD | CREDIT_CARD
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        COD,
        CREDIT_CARD
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductInCartEntity> productInCartEntities = new ArrayList<>();
}