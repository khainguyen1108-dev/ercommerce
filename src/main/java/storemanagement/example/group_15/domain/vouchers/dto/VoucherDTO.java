package storemanagement.example.group_15.domain.vouchers.dto;

import lombok.*;
import storemanagement.example.group_15.domain.vouchers.constant.VoucherType;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {
    private Long id;
    private String name;
    private Long discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private VoucherType type;
    private List<String> condition;
    private Long eventId; // Nếu cần biết liên kết với event nào, chỉ trả về ID
}
