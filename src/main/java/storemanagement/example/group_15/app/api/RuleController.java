package storemanagement.example.group_15.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.rule.RuleRequestDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.rules.service.RuleService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("rules")
public class RuleController {
    @Autowired
    private RuleService ruleService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> create(@RequestBody RuleRequestDTO input){
        try {
            Map<String, Object> output = this.ruleService.create(input);
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
