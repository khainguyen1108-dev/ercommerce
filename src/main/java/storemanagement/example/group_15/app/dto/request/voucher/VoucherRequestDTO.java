package storemanagement.example.group_15.app.dto.request.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import storemanagement.example.group_15.domain.vouchers.constant.VoucherType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VoucherRequestDTO {
    private String name;
    private Long discountValue;
    private String startDate;
    private String endDate;
    private VoucherType type;  // "product", "collection", "event", "all"
    private List<String> condition;  // List of product or collection IDs
}
