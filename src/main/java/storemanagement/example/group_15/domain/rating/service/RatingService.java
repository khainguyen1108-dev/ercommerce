package storemanagement.example.group_15.domain.rating.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.rating.RatingRequestDTO;
import storemanagement.example.group_15.app.dto.request.rating.RatingRequestParamDTO;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;
import storemanagement.example.group_15.domain.rating.entity.RatingEntity;
import storemanagement.example.group_15.domain.rating.repository.RatingRepository;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthRepository authRepository;
    public RatingEntity create(RatingRequestDTO input){
        Optional<ProductEntity> product = this.productRepository.findById(input.getProductId());
        if (product.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "product_id.not_found");
        }
        Optional<AuthEntity> customer = this.authRepository.findById(input.getCustomerId());
        if (customer.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "customer_id.not_found");
        }
        RatingEntity rating = new RatingEntity();
        rating.setProduct(product.get());
        rating.setCustomer(customer.get());
        rating.setStars(input.getStars());
        rating.setDescription(input.getDescription());
        return this.ratingRepository.save(rating);
    }
    public List<RatingEntity> findAll(RatingRequestParamDTO input){
        List<RatingEntity> output = this.ratingRepository.findByProductId(input.getProductId());
        return output;
    }
    public Long update(Long id, RatingRequestDTO input){
        Optional<RatingEntity> output = this.ratingRepository.findById(id);
        if (output.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "rating_id.not_found");
        }
        output.get().setDescription(input.getDescription());
        output.get().setStars(input.getStars());
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
