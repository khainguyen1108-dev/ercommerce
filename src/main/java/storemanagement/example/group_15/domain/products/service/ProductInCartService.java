package storemanagement.example.group_15.domain.products.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import storemanagement.example.group_15.domain.carts.entity.CartEntity;
import storemanagement.example.group_15.domain.carts.repository.CartRepository;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.entity.ProductInCartEntity;
import storemanagement.example.group_15.domain.products.repository.ProductInCartRepository;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

@Service
public class ProductInCartService {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductInCartRepository productInCartRepository;

  @Autowired
  private CartRepository cartRepository;

  public void addProductToCart(Long userId, Long productId, Integer quantity) {
    CartEntity cart = cartRepository.findByCustomerId(userId).orElseThrow(
        () -> new AppException(HttpStatus.NOT_FOUND, "Cart not found"));

    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found"));

    Optional<ProductInCartEntity> existingItem = productInCartRepository.findByCartIdAndProductId(cart.getId(),
        productId);

    if (existingItem.isPresent()) {
      ProductInCartEntity item = existingItem.get();
      int currentQuantity = item.getQuantity();
      int inventoryQuantity = product.getInventory().intValue();
      int newQuantity = currentQuantity + quantity > inventoryQuantity
          ? inventoryQuantity
          : currentQuantity + quantity;

      item.setQuantity(newQuantity);
      productInCartRepository.save(item);
    } else {
      ProductInCartEntity item = new ProductInCartEntity();
      int inventoryQuantity = product.getInventory().intValue();
      int newQuantity = quantity > inventoryQuantity
          ? inventoryQuantity
          : quantity;

      item.setCart(cart);
      item.setProduct(product);
      item.setQuantity(newQuantity);
      productInCartRepository.save(item);
    }
  }

  public void updateProductQuantity(Long userId, Long productId, Integer quantity) {
    CartEntity cart = cartRepository.findByCustomerId(userId).orElseThrow(
        () -> new AppException(HttpStatus.NOT_FOUND, "Cart not found"));

    ProductInCartEntity item = productInCartRepository.findByCartIdAndProductId(cart.getId(), productId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found in cart"));

    if (quantity <= 0) {
      productInCartRepository.delete(item);
    } else {
      ProductEntity product = productRepository.findById(productId)
          .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found"));

      int inventoryQuantity = product.getInventory().intValue();
      int newQuantity = quantity > inventoryQuantity
          ? inventoryQuantity
          : quantity;

      item.setQuantity(newQuantity);
      productInCartRepository.save(item);
    }
  }

  public void removeProductFromCart(Long userId, Long productId) {
    CartEntity cart = cartRepository.findByCustomerId(userId).orElseThrow(
        () -> new AppException(HttpStatus.NOT_FOUND, "Cart not found"));

    ProductInCartEntity item = productInCartRepository.findByCartIdAndProductId(cart.getId(), productId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found in cart"));

    productInCartRepository.delete(item);
  }

}
