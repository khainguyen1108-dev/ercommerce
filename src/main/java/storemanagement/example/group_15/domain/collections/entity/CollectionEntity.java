package storemanagement.example.group_15.domain.collections.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();
}