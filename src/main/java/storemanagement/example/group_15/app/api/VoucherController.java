package storemanagement.example.group_15.app.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.voucher.VoucherRequestDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherResponseDTO;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import storemanagement.example.group_15.domain.vouchers.service.VoucherService;

import java.util.Map;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    //add
    @PostMapping("")
    public ResponseEntity<ApiResponse<VoucherResponseDTO>> create(@RequestBody @Valid VoucherRequestDTO input, HttpServletRequest request){
        try{
            VoucherResponseDTO output = this.voucherService.create(input);
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //update
    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> delete(@PathVariable Long id){
        try {
            Map<String, Long> output = this.voucherService.delete(id);
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //getAll
    //getById
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VoucherResponseDTO>> getById(@PathVariable Long id){
        try{
            VoucherResponseDTO output = this.voucherService.getById(id);
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
