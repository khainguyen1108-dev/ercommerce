package storemanagement.example.group_15.domain.orders.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private AuthEntity customer;

    @Column(name = "total_payment", nullable = false)
    private BigDecimal totalPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private VoucherEntity voucher;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false)
    private boolean status;

    // JSON field to store product details
    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "products")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Map<String, Object>> products;

    public enum PaymentMethod {
        COD,
        CREDIT_CARD
    }
}