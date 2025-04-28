package storemanagement.example.group_15.domain.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.product.ProductCreateDTO;
import storemanagement.example.group_15.domain.auth.entity.AuthEntity;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductEntity create(ProductCreateDTO input) {
        ProductEntity dataCreate = new ProductEntity();
        dataCreate.setName(input.getName());
        dataCreate.setDescription(input.getDesc());
        dataCreate.setImg(input.getImg());
        dataCreate.setInventory(input.getInventory());
        dataCreate.setPrice(input.getPrice());
        dataCreate.setVendor(input.getVendor());
        this.productRepository.save(dataCreate);
        return dataCreate;
    }

    public Long update(ProductCreateDTO input, Number id) {
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
        this.productRepository.deleteById(id);
        return id;
    }

    public ProductEntity getById(Long id) {
        return this.productRepository.getById(id);
    }

    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
