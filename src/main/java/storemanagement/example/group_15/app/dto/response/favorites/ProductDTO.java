package storemanagement.example.group_15.app.dto.response.favorites;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private Long inventory;
    private Long sold;
    private String name;
    private String description;
    private Double price;
    private String img;
    private String vendor;
}