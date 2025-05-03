package storemanagement.example.group_15.app.api;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.favorites.FavoritesRequestDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.app.dto.response.favorites.FavoritesResponseDTO;
import storemanagement.example.group_15.domain.favorites.service.FavoritesService;


@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<FavoritesResponseDTO>> create(@RequestBody @Valid FavoritesRequestDTO input){
      try{
          FavoritesResponseDTO output = this.favoritesService.create(input);
          return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
     } catch (Exception e) {
        throw new RuntimeException(e);}
    }
    @GetMapping("")
    public ResponseEntity<ApiResponse<FavoritesResponseDTO>> create(HttpServletRequest request){
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String sub = (String) claims.get("sub");
            FavoritesResponseDTO output = this.favoritesService.getAll(Long.parseLong(sub));
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
        } catch (Exception e) {
            throw new RuntimeException(e);}
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long id,HttpServletRequest request){
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String sub = (String) claims.get("sub");
            Long output = this.favoritesService.delete(id,Long.parseLong(sub));
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success", output, SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
