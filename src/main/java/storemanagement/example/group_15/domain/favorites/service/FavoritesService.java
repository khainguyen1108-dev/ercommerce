package storemanagement.example.group_15.domain.favorites.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.favorites.FavoritesRequestDTO;
import storemanagement.example.group_15.app.dto.response.favorites.CustomerDTO;
import storemanagement.example.group_15.app.dto.response.favorites.FavoritesResponseDTO;
import storemanagement.example.group_15.app.dto.response.favorites.ProductDTO;
import storemanagement.example.group_15.domain.favorites.entity.FavoritesEntity;
import storemanagement.example.group_15.domain.favorites.repository.FavoritesRepository;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritesService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    public FavoritesResponseDTO create(FavoritesRequestDTO input) {
        Optional<AuthEntity> customerOpt = this.authRepository.findById(input.getCustomer_id());
        if (customerOpt.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "customer_id.not_found");
        }
        AuthEntity customer = customerOpt.get();

        List<FavoritesEntity> favoritesEntities = new ArrayList<>();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Long item : input.getProduct_id()) {
            ProductDTO productDTO = new ProductDTO();
            boolean exists = this.favoritesRepository.existsByCustomerIdAndProductId(customer.getId(), item);
            if (exists) {
                throw new AppException(HttpStatus.BAD_REQUEST, "favorite_already_exists: customer_id=" + customer.getId() + ", product_id=" + item);
            }
            Optional<ProductEntity> productOpt = this.productRepository.findById(item);
            if (productOpt.isEmpty()) {
                throw new AppException(HttpStatus.BAD_REQUEST, "product_id.not_found");
            }
            ProductEntity product = productOpt.get();
            FavoritesEntity fa = new FavoritesEntity();
            fa.setCustomer(customer);
            fa.setProduct(product);
            favoritesEntities.add(fa);
            productDTO.setId(product.getId());
            productDTO.setInventory(product.getInventory());
            productDTO.setSold(product.getSold());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setImg(product.getImg());
            productDTO.setCollectionId(product.getCollectionId());
            productDTO.setVendor(product.getVendor());
            productDTOs.add(productDTO);
        }
        this.favoritesRepository.saveAll(favoritesEntities);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName()); // T
        FavoritesResponseDTO response = new FavoritesResponseDTO();
        response.setCustomer(customerDTO);
        response.setProducts(productDTOs);
        return response;
    }
    public FavoritesResponseDTO getAll(Long id){
        Optional<AuthEntity> customerOpt = this.authRepository.findById(id);
        if (customerOpt.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "customer_id.not_found");
        }
        List<FavoritesEntity> favoritesEntities = this.favoritesRepository.findAllByCustomerId(id);
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (FavoritesEntity favorite : favoritesEntities) {
            ProductEntity product = favorite.getProduct();
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setInventory(product.getInventory());
            productDTO.setSold(product.getSold());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setImg(product.getImg());
            productDTO.setCollectionId(product.getCollectionId());
            productDTO.setVendor(product.getVendor());
            productDTOs.add(productDTO);
        }
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerOpt.get().getId());
        customerDTO.setName(customerOpt.get().getName());
        FavoritesResponseDTO response = new FavoritesResponseDTO();
        response.setProducts(productDTOs);
        response.setCustomer(customerDTO);
        return response;
    }
    public Long delete(Long product_id, Long id){
        Optional<FavoritesEntity> fDelete = this.favoritesRepository.findByProductIdAndCustomerId(product_id, id);
        if (fDelete.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "favorites_id.not_found");
        }
     this.favoritesRepository.delete(fDelete.get());
        return product_id;
    }
}
