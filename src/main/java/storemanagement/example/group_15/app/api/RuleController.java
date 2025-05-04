package storemanagement.example.group_15.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.rule.RuleRequestDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.rules.dto.RuleDTO;
import storemanagement.example.group_15.domain.rules.service.RuleService;

import java.util.List;
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> delete(@RequestParam Long id){
        try {
            Long output = this.ruleService.delete(id);
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<RuleDTO>>> getAll(){
        try {
            List<RuleDTO> output = this.ruleService.getAll();
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
