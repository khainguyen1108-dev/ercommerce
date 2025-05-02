package storemanagement.example.group_15.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.response.cart.CartResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.carts.service.CartService;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.AuthHelper;

@RestController()
@RequestMapping("/carts")
public class CartController {
  private static final Logger log = LoggerFactory.getLogger(EventController.class);

  @Autowired
  private CartService cartService;

  @PostMapping()
  public ResponseEntity<ApiResponse<CartResponseDTO>> createCart(HttpServletRequest request) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);
      CartResponseDTO cart = cartService.createCart(userId);
      return ResponseEntity.status(SuccessConstant.CREATED).body(
          ApiResponse.success(SuccessConstant.SUCCESS, cart, 201));
    } catch (Exception e) {
      log.error("createCart", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating cart: " + e.getMessage());
    }
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(HttpServletRequest request) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);
      CartResponseDTO cart = cartService.getCartByUserId(userId);
      return ResponseEntity.status(SuccessConstant.OK).body(
          ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("getCartByUserId", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting cart: " + e.getMessage());
    }
  }

  @DeleteMapping()
  public ResponseEntity<ApiResponse<Void>> resetCart(HttpServletRequest request) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);
      cartService.resetCart(userId);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, null, 200));
    } catch (Exception e) {
      log.error("DeleteCart", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reset cart: " + e.getMessage());
    }
  }

  // ADD PRODUCT TO CART
  @PostMapping("/add")
  public ResponseEntity<ApiResponse<CartResponseDTO>> addProductToCart(
      HttpServletRequest request,
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);
      CartResponseDTO cart = cartService.addProductToCart(userId, productId, quantity);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("addProductToCart", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error add product to cart: " + e.getMessage());
    }
  }

  // REMOVE A PRODUCT FROM CART
  @DeleteMapping("/remove/{productId}")
  public ResponseEntity<ApiResponse<CartResponseDTO>> removeProductFromCart(
      HttpServletRequest request,
      @PathVariable Long productId) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);
      CartResponseDTO cart = cartService.removeProductFromCart(userId, productId);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("removeProductFromCart", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error remove product from cart: " + e.getMessage());
    }
  }

  // INCREASE/DECREASE QUANTITY A PRODUCT IN CART
  @PutMapping("/update/products/{productId}")
  public ResponseEntity<ApiResponse<CartResponseDTO>> updateProductQuantity(
      HttpServletRequest request,
      @PathVariable Long productId,
      @RequestParam Integer quantity) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);
      CartResponseDTO cart = cartService.updateProductQuantityInCart(userId, productId, quantity);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, cart, 200));
    } catch (Exception e) {
      log.error("updateProductQuantity", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Error update product quantity in cart: " + e.getMessage());
    }
  }
}
