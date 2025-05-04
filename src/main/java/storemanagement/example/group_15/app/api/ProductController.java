package storemanagement.example.group_15.app.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.product.ProductCreateDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.products.dto.ProductDto;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.service.ProductService;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

@RestController()
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDto> products = productService.getAllProducts(pageable,minPrice, maxPrice, fromDate, toDate);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, products, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getById(@PathVariable Long id) {
        try {
            ProductDto output = this.productService.getById(id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 200));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Long>> create(@RequestBody @Valid ProductCreateDTO product) {
        try {
            Long output = this.productService.create(product);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Number>> delete(@PathVariable Long id) {
        try {
            Long output = this.productService.delete(id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 204));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Number>> update(@PathVariable Long id, @RequestBody ProductCreateDTO product) {
        try {
            Long output = this.productService.update(product, id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 200));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
