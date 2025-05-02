package storemanagement.example.group_15.app.dto.response.cart;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import storemanagement.example.group_15.app.dto.response.product.ProductInCartResponseDTO;
import storemanagement.example.group_15.domain.carts.constants.PaymentMethod;

@Data
@AllArgsConstructor
public class CartResponseDTO {
  private BigDecimal totalPrice;
  private BigDecimal totalPayment;
  private Long voucherId;
  private Long customerId;
  private PaymentMethod paymentMethod;
  private List<ProductInCartResponseDTO> products;

}
