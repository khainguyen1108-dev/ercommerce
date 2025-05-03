package storemanagement.example.group_15.app.dto.response.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import storemanagement.example.group_15.app.dto.response.user.UserResponseDTO;
import storemanagement.example.group_15.app.dto.response.voucher.VoucherResponseDTO;
import storemanagement.example.group_15.domain.carts.constants.PaymentMethod;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
  private Long id;
  private UserResponseDTO customer;
  private BigDecimal totalPrice;
  private BigDecimal totalPayment;
  private VoucherResponseDTO voucher;
  private Boolean status;
  private PaymentMethod paymentMethod;
  private String products;
}
