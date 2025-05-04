package storemanagement.example.group_15.app.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.collections.CollectionRequestDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.collections.dto.CollectionDTO;
import storemanagement.example.group_15.domain.collections.entity.CollectionEntity;
import storemanagement.example.group_15.domain.collections.service.CollectionService;

import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    public CollectionService collectionService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Long>> create(@RequestBody @Valid CollectionRequestDTO input){
       try{
           Long output = this.collectionService.create(input);
           return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> update(@PathVariable Long id, @RequestBody @Valid CollectionRequestDTO input){
    try{
        Long output = this.collectionService.update(id,input);
        return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long id){
        try{
            Long output = this.collectionService.delete(id);
            return ResponseEntity.status(SuccessConstant.CREATED).body(ApiResponse.success("success",output,SuccessConstant.CREATED));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<CollectionDTO> >> getAll(){
        try{
            List<CollectionDTO>  output = this.collectionService.getAll();
            return ResponseEntity.status(SuccessConstant.OK).body(ApiResponse.success("success",output,SuccessConstant.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
