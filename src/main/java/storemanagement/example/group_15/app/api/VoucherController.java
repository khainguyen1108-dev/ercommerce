package storemanagement.example.group_15.app.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.voucher.VoucherRequestDTO;
import storemanagement.example.group_15.app.dto.response.cart.CartResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherDTO;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherResponseDTO;
import storemanagement.example.group_15.domain.vouchers.service.VoucherService;
import storemanagement.example.group_15.infrastructure.helper.AuthHelper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<VoucherResponseDTO>> create(@RequestBody @Valid VoucherRequestDTO input, HttpServletRequest request){
        try{
            VoucherResponseDTO output = this.voucherService.create(input);
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> delete(@PathVariable Long id){
        try {
            Map<String, Long> output = this.voucherService.delete(id);
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Number>> update(@PathVariable Long id, @RequestBody @Valid VoucherRequestDTO input){
        try{
            Long output = this.voucherService.update(id, input);
            return  ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success", output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VoucherResponseDTO>> getById(@PathVariable Long id){
        try{
            VoucherResponseDTO output = this.voucherService.getById(id);
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/calcPrice")
    public ResponseEntity<ApiResponse<String>> calcPrice(@RequestBody Map<String, Long> body , HttpServletRequest request){
        try{
            Long voucher_id = body.get("voucher_id");
            Long customer_id = AuthHelper.getUserIdFromRequest(request);
            String data =  this.voucherService.calcPrice(voucher_id,customer_id);
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",data,SuccessConstant.OK));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> getALl(){
        try{
            List<VoucherDTO> data =  this.voucherService.getAll();
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",data,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
