package storemanagement.example.group_15.app.dto.request.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleRequestDTO {
    private String name;
    private Set<String> permissions;
}
