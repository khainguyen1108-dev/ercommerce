package storemanagement.example.group_15.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.permissions.dto.PermissionDTO;
import storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;
import storemanagement.example.group_15.domain.permissions.service.PermissionsService;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("permissions")
public class PermissionController {

    @Autowired
    private PermissionsService permissionsService;

    @PostMapping()
    public ResponseEntity<ApiResponse< Map<String, Set<String>>>> create(@RequestBody Map<String, Set<String>> input){
        try{
            Map<String, Set<String>> output = this.permissionsService.create(input);
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getAll(){
        try{
            List<PermissionDTO> output = this.permissionsService.getAll();
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> getAll(@RequestParam Long id, @RequestBody Map<String, String> input){
        try{
            Long output = this.permissionsService.update(id, input);
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> getAll(@RequestParam Long id){
        try{
            Long output = this.permissionsService.delete(id);
            return ResponseEntity.status(SuccessConstant.CREATED).body(
                    ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
