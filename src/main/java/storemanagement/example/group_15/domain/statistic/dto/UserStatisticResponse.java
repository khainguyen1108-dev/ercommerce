package storemanagement.example.group_15.domain.statistic.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import storemanagement.example.group_15.app.dto.response.product.ProductInCartResponseDTO;
import storemanagement.example.group_15.app.dto.response.user.UserResponseDTO;

@Data
@AllArgsConstructor
public class UserStatisticResponse {
  private UserResponseDTO customer;
  private BigDecimal totalRevenue;
  private Long totalOrder;
  private List<ProductInCartResponseDTO> products;
}
