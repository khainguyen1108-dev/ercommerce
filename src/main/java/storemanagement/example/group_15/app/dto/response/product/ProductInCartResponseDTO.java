package storemanagement.example.group_15.app.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductInCartResponseDTO {
  private Number id;
  private String name;
  private String desc;
  private String price;
  private Integer quantity;
}
