package storemanagement.example.group_15.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.app.dto.response.order.OrderResponseDTO;
import storemanagement.example.group_15.domain.orders.service.OrderService;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.AuthHelper;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private static final Logger log = LoggerFactory.getLogger(EventController.class);

  @Autowired
  private OrderService orderService;

  @PostMapping("/checkout")
  public ResponseEntity<ApiResponse<OrderResponseDTO>> checkout(HttpServletRequest request) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);

      OrderResponseDTO order = orderService.createOrder(userId);

      return ResponseEntity.status(SuccessConstant.CREATED).body(
          ApiResponse.success(SuccessConstant.SUCCESS, order, 201));
    } catch (Exception e) {
      log.error("checkout", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when checkout: " + e.getMessage());
    }
  }

  // TODO: only admin can update status
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<OrderResponseDTO>> updateStatusOrder(HttpServletRequest request, Long orderId,
      boolean status) {
    try {
      Long userId = AuthHelper.getUserIdFromRequest(request);

      OrderResponseDTO order = orderService.updateStatusOrder(orderId, status);

      return ResponseEntity.status(SuccessConstant.CREATED).body(
          ApiResponse.success(SuccessConstant.SUCCESS, order, 201));
    } catch (Exception e) {
      log.error("update status", e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when update status: " + e.getMessage());
    }
  }
}
