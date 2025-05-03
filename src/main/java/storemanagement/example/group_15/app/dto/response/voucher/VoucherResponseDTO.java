package storemanagement.example.group_15.app.dto.response.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherResponseDTO {
  private Long id;
  private String name;
  private Long discountValue;
}
