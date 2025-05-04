package storemanagement.example.group_15.domain.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;

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
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.repository.ProductRepository;
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
  private ProductRepository productRepository;

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
        order.getProducts(),
        order.getCreatedAt(),
        order.getUpdatedAt());
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

    // save order
    orderRepository.save(order);

    // change product stock
    for (var product : cart.getProducts()) {
      ProductEntity productEntity = productRepository.findById(product.getId().longValue())
          .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found"));

      if (productEntity.getInventory() <= 0) {
        throw new AppException(HttpStatus.BAD_REQUEST, "Product " + productEntity.getName() + " is out of stock");
      } else if (productEntity.getInventory() < product.getQuantity()) {
        throw new AppException(HttpStatus.BAD_REQUEST, "Product " + productEntity.getName() + " is not enough stock");
      }

      Long soldQuantity = productEntity.getSold() == null ? 0L : productEntity.getSold();
      productEntity.setInventory(productEntity.getInventory() - product.getQuantity().longValue());
      productEntity.setSold(soldQuantity + product.getQuantity().longValue());
      productRepository.save(productEntity);
    }

    // reset cart
    cartService.resetCart(userId);

    // update userInfo
    authRepository.updateIsBuyById(userId, true);

    return convertOrderDTO(order);
  }

  public OrderResponseDTO updateStatusOrder(Long orderId, boolean status) {
    OrderEntity order = orderRepository.findById(orderId)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Order not found"));

    order.setStatus(status);
    orderRepository.save(order);

    return convertOrderDTO(order);
  }

  public void deleteOrder(Long orderId) {
    orderRepository.deleteById(orderId);
  }

  public OrderResponseDTO getOrderById(Long id) {
    OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Order not found"));

    return convertOrderDTO(order);
  }

  public Page<OrderResponseDTO> getOrders(Long userId, Pageable pageable) {
    Page<OrderEntity> orders = orderRepository.findAllByCustomerId(userId, pageable);

    return orders.map(this::convertOrderDTO);
  }

  public Page<OrderResponseDTO> getOrders(
      Long userId,
      int page,
      int size,
      String sortDirection,
      LocalDate startDate,
      LocalDate endDate) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "createdAt");
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<OrderEntity> orders;

    if (startDate != null && endDate != null) {
      orders = orderRepository.findByCustomerIdAndCreatedAtBetween(
          userId,
          startDate.atStartOfDay(),
          endDate.atTime(LocalTime.MAX),
          pageable);
    } else if (startDate != null) {
      orders = orderRepository.findByCustomerIdAndCreatedAtAfter(
          userId,
          startDate.atStartOfDay(),
          pageable);
    } else if (endDate != null) {
      orders = orderRepository.findByCustomerIdAndCreatedAtBefore(
          userId,
          endDate.atTime(LocalTime.MAX),
          pageable);
    } else {
      orders = orderRepository.findAllByCustomerId(userId, pageable);
    }

    return orders.map(this::convertOrderDTO);
  }

}
