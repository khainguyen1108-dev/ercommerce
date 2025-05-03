package storemanagement.example.group_15.domain.rating.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.rating.RatingRequestDTO;
import storemanagement.example.group_15.app.dto.request.rating.RatingRequestParamDTO;
import storemanagement.example.group_15.app.dto.response.favorites.CustomerDTO;
import storemanagement.example.group_15.app.dto.response.favorites.ProductDTO;
import storemanagement.example.group_15.app.dto.response.rating.RatingResponseDTO;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;
import storemanagement.example.group_15.domain.rating.entity.RatingEntity;
import storemanagement.example.group_15.domain.rating.repository.RatingRepository;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthRepository authRepository;
    public RatingResponseDTO create(RatingRequestDTO input, Long customer_id) {
        Optional<RatingEntity> rat = this.ratingRepository.findByProductIdAndCustomerId(input.getProductId(), customer_id);
        if (rat.isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "rating_id.existed");
        }

        Optional<ProductEntity> product = this.productRepository.findById(input.getProductId());
        if (product.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "product_id.not_found");
        }

        Optional<AuthEntity> customer = this.authRepository.findById(customer_id);
        if (customer.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "customer_id.not_found");
        }

        RatingEntity rating = new RatingEntity();
        rating.setProduct(product.get());
        rating.setCustomer(customer.get());
        rating.setStars(input.getStars());
        rating.setDescription(input.getDescription());
        this.ratingRepository.save(rating);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.get().getId());
        productDTO.setInventory(product.get().getInventory());
        productDTO.setSold(product.get().getSold());
        productDTO.setName(product.get().getName());
        productDTO.setDescription(product.get().getDescription());
        productDTO.setPrice(product.get().getPrice());
        productDTO.setImg(product.get().getImg());
        productDTO.setVendor(product.get().getVendor());

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(customer.get().getName());
        customerDTO.setId(customer.get().getId());

        RatingResponseDTO response = new RatingResponseDTO();
        response.setId(rating.getId());
        response.setCustomer(customerDTO);
        response.setProducts(productDTO);
        response.setStars(input.getStars());
        response.setDescription(input.getDescription());

        return response;
    }
    public List<RatingResponseDTO> findAll(RatingRequestParamDTO input) {
        Long productId = input.getProductId();
        if (productId == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "product_id.required");
        }

        // Kiểm tra product tồn tại
        Optional<ProductEntity> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "product_id.not_found: " + productId);
        }

        List<RatingEntity> ratings = ratingRepository.findByProductId(productId);
        return ratings.stream().map(rating -> {
            RatingResponseDTO dto = new RatingResponseDTO();
            dto.setId(rating.getId());
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(rating.getCustomer().getId());
            customerDTO.setName(rating.getCustomer().getName());
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(rating.getProduct().getId());
            productDTO.setInventory(rating.getProduct().getInventory());
            productDTO.setSold(rating.getProduct().getSold());
            productDTO.setName(rating.getProduct().getName());
            productDTO.setDescription(rating.getProduct().getDescription());
            productDTO.setPrice(rating.getProduct().getPrice());
            productDTO.setImg(rating.getProduct().getImg());
            productDTO.setVendor(rating.getProduct().getVendor());
            dto.setCustomer(customerDTO);
            dto.setProducts(productDTO);
            dto.setStars(rating.getStars());
            dto.setDescription(rating.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }
    public Long update(Long id, RatingRequestDTO input){
        Optional<RatingEntity> output = this.ratingRepository.findById(id);
        if (output.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "rating_id.not_found");
        }
        if (input.getDescription() != null && !input.getDescription().isBlank()) {
            output.get().setDescription(input.getDescription());
        }

        if (input.getStars() != null) {
            output.get().setStars(input.getStars());
        }

        RatingEntity out = this.ratingRepository.save(output.get());
        return out.getId();
    }
    public Long delete(Long id){
        Optional<RatingEntity> out = this.ratingRepository.findById(id);
        if (out.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "rating_id.not_found");
        }
        this.ratingRepository.delete(out.get());
        return id;
    }
}
