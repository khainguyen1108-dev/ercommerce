package storemanagement.example.group_15.domain.vouchers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import storemanagement.example.group_15.domain.vouchers.constant.VoucherType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherResponseDTO {
    private Long id;
    private String name;
    private Long discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private VoucherType type;
    private List<String> conditions;
    private boolean isApplicable;

    // Bạn có thể thêm các trường khác nếu cần
}