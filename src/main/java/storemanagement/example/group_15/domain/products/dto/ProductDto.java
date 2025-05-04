package storemanagement.example.group_15.domain.products.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private Long inventory;
    private Long sold;
    private String name;
    private String description;
    private Double price;
    private String img;
    private String vendor;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
