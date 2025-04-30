package storemanagement.example.group_15.domain.events.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "time_at", nullable = false)
    private LocalDateTime timeAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "voucher_id")
    private VoucherEntity voucher;
}