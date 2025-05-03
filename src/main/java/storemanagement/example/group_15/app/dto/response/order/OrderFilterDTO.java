package storemanagement.example.group_15.app.dto.response.order;

import java.time.LocalDate;

import lombok.Data;

@Data
public class OrderFilterDTO {
  private int page = 0;
  private int size = 10;
  private String sortDirection = "DESC";
  private LocalDate startDate;
  private LocalDate endDate;
}
