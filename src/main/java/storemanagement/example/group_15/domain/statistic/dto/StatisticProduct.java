package storemanagement.example.group_15.domain.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticProduct {
  private Number id;
  private String name;
  private Integer quantity;
}
