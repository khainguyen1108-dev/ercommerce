package storemanagement.example.group_15.app.api;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.product.ProductCreateDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.service.ProductService;
import org.springframework.data.domain.PageRequest;


@RestController()
@RequestMapping("/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    //    READ -> get all product
//    @GetMapping("")
//    public void getAll(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ProductEntity> products = productService.getAllProducts(pageable);gi
//    }


    //READ -> get product by id
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse> getById() {
//
//    }

    //CREATE -> create product
    @PostMapping()
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid ProductCreateDTO product) {
        try {
            ProductEntity output = this.productService.create(product);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            log.error("createProduct", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //DELETE -> delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        try {
            Long output = this.productService.delete(id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            log.error("deleteProduct", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //
    //PUT -> update product
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody ProductCreateDTO product) {
        try {
            Long output = this.productService.update(product, id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            log.error("deleteProduct", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
