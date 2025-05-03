package storemanagement.example.group_15.domain.statistic.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueResponseDTO {
  private BigDecimal revenue;
  private Long totalOrder;
}
