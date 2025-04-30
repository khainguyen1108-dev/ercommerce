package storemanagement.example.group_15.domain.products.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.collections.entity.CollectionEntity;
import storemanagement.example.group_15.domain.favorites.entity.FavoritesEntity;
import storemanagement.example.group_15.domain.rating.entity.RatingEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "inventory", nullable = false)
    private Integer inventory;

    @Column(name = "sold", nullable = false)
    private Integer sold;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "img", length = 500)
    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = true)
    private CollectionEntity collection;

    @Column(name = "vendor", length = 255)
    private String vendor;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FavoritesEntity> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductInCartEntity> productInCarts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RatingEntity> ratings = new ArrayList<>();
}
