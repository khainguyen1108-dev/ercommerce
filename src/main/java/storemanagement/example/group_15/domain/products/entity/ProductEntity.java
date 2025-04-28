package storemanagement.example.group_15.domain.products.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory")
    private Long inventory;

    @Column(name = "sold")
    private Long sold;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "linhtinh")
    private String linhtinh;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "img", length = 500)
    private String img;

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "vendor", length = 255)
    private String vendor;
}