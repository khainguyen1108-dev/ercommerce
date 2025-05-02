package storemanagement.example.group_15.domain.orders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import storemanagement.example.group_15.app.dto.response.cart.CartResponseDTO;
import storemanagement.example.group_15.app.dto.response.order.OrderResponseDTO;
import storemanagement.example.group_15.app.dto.response.user.UserResponseDTO;
import storemanagement.example.group_15.app.dto.response.voucher.VoucherResponseDTO;
import storemanagement.example.group_15.domain.carts.service.CartService;
import storemanagement.example.group_15.domain.orders.entity.OrderEntity;
import storemanagement.example.group_15.domain.orders.repository.OrderRepository;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import storemanagement.example.group_15.domain.vouchers.repository.VoucherRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

@Service
public class OrderService {
  @Autowired
  private CartService cartService;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private AuthRepository authRepository;

  @Autowired
  private VoucherRepository voucherRepository;

  @Autowired
  private storemanagement.example.group_15.infrastructure.helper.JsonHelper jsonHelper;

  public OrderResponseDTO convertOrderDTO(OrderEntity order) {
    UserResponseDTO user = new UserResponseDTO(
        order.getCustomer().getId(),
        order.getCustomer().getEmail(),
        order.getCustomer().getName(),
        order.getCustomer().getPhone(),
        order.getCustomer().getAddress(),
        order.getCustomer().getIsBuy());

    VoucherResponseDTO voucher = order.getVoucher() != null ? new VoucherResponseDTO(
        order.getVoucher().getId(),
        order.getVoucher().getName(),
        order.getVoucher().getDiscountValue()) : null;

    return new OrderResponseDTO(
        order.getId(),
        user,
        order.getTotalPrice(),
        order.getTotalPayment(),
        voucher,
        order.isStatus(),
        order.getPaymentMethod(),
        order.getProducts());
  }

  @Transactional
  public OrderResponseDTO createOrder(Long userId) {
    CartResponseDTO cart = cartService.getCartByUserId(userId);
    if (cart.getProducts().size() == 0) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Cart is empty");
    }

    VoucherEntity voucher = null;
    if (cart.getVoucherId() != null) {
      voucher = voucherRepository.findById(cart.getVoucherId()).orElse(null);
    }

    OrderEntity order = OrderEntity.builder()
        .customer(
            authRepository.findById(userId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found")))
        .totalPrice(cart.getTotalPrice())
        .totalPayment(cart.getTotalPayment())
        .voucher(voucher)
        .paymentMethod(cart.getPaymentMethod())
        .status(false)
        .products(jsonHelper.convertProductsToJsonArray(cart.getProducts()))
        .build();

    orderRepository.save(order);
    cartService.resetCart(userId);

    return convertOrderDTO(order);
  }

  public OrderResponseDTO updateStatusOrder(Long orderId, boolean status) {
    OrderEntity order = orderRepository.findById(orderId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Order not found"));

    order.setStatus(status);
    orderRepository.save(order);

    return convertOrderDTO(order);
  }

}
