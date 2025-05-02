package storemanagement.example.group_15.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.response.cart.CartResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.carts.service.CartService;

// TODO: nhan biet user tu requets => cart cua user do
@RestController()
@RequestMapping("/carts")
public class CartController {
  private static final Logger log = LoggerFactory.getLogger(EventController.class);

  @Autowired
  private CartService cartService;

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<CartResponseDTO>> createCart(@RequestParam Long userId) {
    try {
      CartResponseDTO cart = cartService.createCart(userId);
      return ResponseEntity.status(SuccessConstant.CREATED).body(
          ApiResponse.success(SuccessConstant.SUCCESS, cart, 201));
    } catch (Exception e) {
      log.error("createCart", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(@PathVariable Long userId) {
    try {
      CartResponseDTO cart = cartService.getCartByUserId(userId);
      return ResponseEntity.status(SuccessConstant.OK).body(
          ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("getCartByUserId: " + userId, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<Void>> resetCart(@PathVariable Long userId) {
    try {
      cartService.resetCart(userId);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, null, 200));
    } catch (Exception e) {
      log.error("DeleteCart", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  // ADD PRODUCT TO CART
  @PostMapping("/{userId}/products")
  public ResponseEntity<ApiResponse<CartResponseDTO>> addProductToCart(
      @PathVariable Long userId,
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      CartResponseDTO cart = cartService.addProductToCart(userId, productId, quantity);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("addProductToCart", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  // REMOVE A PRODUCT FROM CART
  @DeleteMapping("/{userId}/products/{productId}")
  public ResponseEntity<ApiResponse<CartResponseDTO>> removeProductFromCart(
      @PathVariable Long userId,
      @PathVariable Long productId) {
    try {
      CartResponseDTO cart = cartService.removeProductFromCart(userId, productId);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("removeProductFromCart", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  // INCREASE/DECREASE QUANTITY A PRODUCT IN CART
  @PutMapping("/{userId}/products/{productId}")
  public ResponseEntity<ApiResponse<CartResponseDTO>> updateProductQuantity(
      @PathVariable Long userId,
      @PathVariable Long productId,
      @RequestParam Integer quantity) {
    try {
      CartResponseDTO cart = cartService.updateProductQuantityInCart(userId, productId, quantity);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("updateProductQuantity", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
