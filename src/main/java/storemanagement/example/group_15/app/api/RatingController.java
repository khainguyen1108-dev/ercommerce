package storemanagement.example.group_15.app.api;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.rating.RatingRequestDTO;
import storemanagement.example.group_15.app.dto.request.rating.RatingRequestParamDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.app.dto.response.rating.RatingResponseDTO;
import storemanagement.example.group_15.domain.rating.entity.RatingEntity;
import storemanagement.example.group_15.domain.rating.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @PostMapping("")
    public ResponseEntity<ApiResponse<RatingResponseDTO>> create(@RequestBody @Valid RatingRequestDTO input, HttpServletRequest request){
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String sub = (String) claims.get("sub");
            RatingResponseDTO output = this.ratingService.create(input, Long.parseLong(sub));
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping()
    public ResponseEntity<ApiResponse< List<RatingResponseDTO>>> getAll(@ModelAttribute @Valid RatingRequestParamDTO input){
        try{
            List<RatingResponseDTO> output = this.ratingService.findAll(input);
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> update(@PathVariable Long id, @RequestBody @Valid RatingRequestDTO input){
        try{
            Long output = this.ratingService.update(id, input);
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> update(@PathVariable Long id){
        try{
            Long output = this.ratingService.delete(id);
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
