package storemanagement.example.group_15.domain.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.product.ProductCreateDTO;
import storemanagement.example.group_15.domain.products.dto.ProductDto;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Long create(ProductCreateDTO input) {
        Optional<ProductEntity> product = this.productRepository.findByName(input.getName());
        if (product.isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST, "name.existed");
        }
        ProductEntity dataCreate = new ProductEntity();
        dataCreate.setName(input.getName());
        dataCreate.setDescription(input.getDesc());
        dataCreate.setImg(input.getImg());
        dataCreate.setInventory(input.getInventory());
        dataCreate.setPrice(input.getPrice());
        dataCreate.setVendor(input.getVendor());
        this.productRepository.save(dataCreate);
        return dataCreate.getId();
    }

    public Long update(ProductCreateDTO input, Number id) {
        Optional<ProductEntity> product = this.productRepository.findById(id.longValue());
        if (product.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Product with id " + id + " not found.");
        }
        Optional<ProductEntity> optionalProduct = productRepository.findById(id.longValue());
        if (optionalProduct.isPresent()) {
            ProductEntity existingProduct = optionalProduct.get();
            existingProduct.setName(input.getName());
            existingProduct.setDescription(input.getDesc());
            existingProduct.setImg(input.getImg());
            existingProduct.setInventory(input.getInventory());
            existingProduct.setPrice(input.getPrice());
            existingProduct.setVendor(input.getVendor());
            productRepository.save(existingProduct);
            return existingProduct.getId();

        } else {
            throw new Error("Product with id " + id + " not found.");
        }
    }

    public Long delete(Long id) {
        Optional<ProductEntity> product = this.productRepository.findById(id);
        if (product.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Product with id " + id + " not found.");
        }
        this.productRepository.deleteById(id);
        return id;
    }

    public ProductDto getById(Long id) {
        Optional<ProductEntity> product = this.productRepository.findById(id);
        if (product.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Product with id " + id + " not found.");
        }
        ProductDto productDto = ProductService.toDto(product.get());
        return productDto;
    }

    public Page<ProductDto> getAllProducts(Pageable pageable, Double minPrice, Double maxPrice, LocalDate fromDate, LocalDate toDate) {
        Page<ProductEntity> productPage = productRepository.filterProducts(minPrice, maxPrice, fromDate, toDate, pageable);

        List<ProductDto> dtoList = new ArrayList<>();
        for (ProductEntity item : productPage.getContent()) {
            ProductDto dto = ProductService.toDto(item);
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }
    public static ProductDto toDto(ProductEntity entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .inventory(entity.getInventory())
                .sold(entity.getSold())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .img(entity.getImg())
                .vendor(entity.getVendor())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
