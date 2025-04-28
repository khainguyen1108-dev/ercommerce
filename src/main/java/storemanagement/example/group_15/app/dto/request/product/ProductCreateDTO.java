package storemanagement.example.group_15.app.dto.request.product;

import lombok.Data;

@Data
public class ProductCreateDTO {
    private Long inventory;
    private Long sold;
    private String name;
    private String desc;
    private Double price;
    private String img;
    private Long collectionId;
    private String vendor;
}
