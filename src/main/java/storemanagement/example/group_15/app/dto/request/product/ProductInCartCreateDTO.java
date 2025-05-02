package storemanagement.example.group_15.app.dto.request.product;

import lombok.Data;

@Data
public class ProductInCartCreateDTO {
  private Long productId;
  private Long quantity;
}
