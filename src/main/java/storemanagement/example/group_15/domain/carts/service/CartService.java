package storemanagement.example.group_15.domain.carts.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import storemanagement.example.group_15.app.dto.response.cart.CartResponseDTO;
import storemanagement.example.group_15.app.dto.response.product.ProductInCartResponseDTO;
import storemanagement.example.group_15.domain.carts.constants.CartPrices;
import storemanagement.example.group_15.domain.carts.constants.PaymentMethod;
import storemanagement.example.group_15.domain.carts.entity.CartEntity;
import storemanagement.example.group_15.domain.carts.repository.CartRepository;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.entity.ProductInCartEntity;
import storemanagement.example.group_15.domain.products.repository.ProductInCartRepository;
import storemanagement.example.group_15.domain.products.service.ProductInCartService;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

@Service
@RequiredArgsConstructor
public class CartService {
  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private AuthRepository authRepository;

  @Autowired
  private ProductInCartRepository productInCartRepository;

  @Autowired
  private ProductInCartService productInCartService;

  public CartResponseDTO getCartDTO(CartEntity cart) {
    List<ProductInCartEntity> productsInCart = productInCartRepository.findAllByCartId(cart.getId());
    List<ProductInCartResponseDTO> products = productsInCart.stream()
        .map(productInCart -> {
          ProductEntity product = productInCart.getProduct();
          return new ProductInCartResponseDTO(
              product.getId(),
              product.getName(),
              product.getDescription(),
              product.getPrice().toString(),
              productInCart.getQuantity());
        })
        .collect(Collectors.toList());

    return new CartResponseDTO(
        cart.getTotalPrice(),
        cart.getTotalPayment(),
        cart.getVoucher() != null ? cart.getVoucher().getId() : null,
        cart.getCustomer().getId(),
        cart.getPaymentMethod(),
        products);
  }

  public CartResponseDTO getCartByUserId(Long userId) {
    authRepository.findById(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

    CartEntity cart = cartRepository.findByCustomerId(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + userId));

    return getCartDTO(cart);
  }

  @Transactional
  public CartResponseDTO createCart(Long userId) {
    AuthEntity user = authRepository.findById(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

    Optional<CartEntity> existingCart = cartRepository.findByCustomerId(userId);

    if (existingCart.isPresent()) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Cart already exists for user: " + userId);
    }

    CartEntity cart = CartEntity.builder()
        .customer(user)
        .totalPrice(BigDecimal.ZERO)
        .totalPayment(BigDecimal.ZERO)
        .paymentMethod(PaymentMethod.COD)
        .build();
    cartRepository.save(cart);
    return getCartDTO(cart);
  }

  @Transactional
  public void resetCart(Long userId) {
    authRepository.findById(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

    CartEntity cart = cartRepository.findByCustomerId(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + userId));

    // Default values
    cart.setTotalPrice(BigDecimal.ZERO);
    cart.setTotalPayment(BigDecimal.ZERO);
    cart.setPaymentMethod(PaymentMethod.COD);
    productInCartRepository.deleteAllByCartId(cart.getId());
    cartRepository.save(cart);
  }

  @Transactional
  public CartResponseDTO addProductToCart(Long userId, Long productId, Integer quantity) {
    CartEntity cart = cartRepository.findByCustomerId(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + userId));

    productInCartService.addProductToCart(userId, productId, quantity);

    List<ProductInCartEntity> productsInCart = productInCartRepository.findAllByCartId(cart.getId());

    CartPrices prices = calculateCartPrices(productsInCart, cart);

    cart.setTotalPrice(prices.totalPrice());
    cart.setTotalPayment(prices.totalPayment());
    cartRepository.save(cart);

    return getCartDTO(cart);
  }

  @Transactional
  public CartResponseDTO removeProductFromCart(Long userId, Long productId) {
    CartEntity cart = cartRepository.findByCustomerId(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + userId));

    productInCartService.removeProductFromCart(userId, productId);

    List<ProductInCartEntity> productsInCart = productInCartRepository.findAllByCartId(cart.getId());

    CartPrices prices = calculateCartPrices(productsInCart, cart);

    cart.setTotalPrice(prices.totalPrice());
    cart.setTotalPayment(prices.totalPayment());
    cartRepository.save(cart);

    return getCartDTO(cart);
  }

  @Transactional
  public CartResponseDTO updateProductQuantityInCart(Long userId, Long productId, Integer quantity) {
    CartEntity cart = cartRepository.findByCustomerId(userId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + userId));

    productInCartService.updateProductQuantity(userId, productId, quantity);

    List<ProductInCartEntity> productsInCart = productInCartRepository.findAllByCartId(cart.getId());

    CartPrices prices = calculateCartPrices(productsInCart, cart);

    cart.setTotalPrice(prices.totalPrice());
    cart.setTotalPayment(prices.totalPayment());
    cartRepository.save(cart);

    return getCartDTO(cart);
  }

  private CartPrices calculateCartPrices(List<ProductInCartEntity> productsInCart, CartEntity cart) {
    // cap nhat gia total_price = tong gia cac san pham trong cart
    BigDecimal totalPrice = productsInCart.stream()
        .map(productInCart -> {
          ProductEntity product = productInCart.getProduct();
          BigDecimal price = BigDecimal.valueOf(product.getPrice());
          return price.multiply(BigDecimal.valueOf(productInCart.getQuantity()));
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // cap nhat gia total_payment = total_price - voucher
    BigDecimal totalPayment = totalPrice;
    if (cart.getVoucher() != null) {
      BigDecimal discount = BigDecimal.valueOf(cart.getVoucher().getDiscountValue());
      totalPayment = totalPrice.subtract(totalPrice.multiply(discount));
    }

    return new CartPrices(totalPrice, totalPayment);
  }
}
