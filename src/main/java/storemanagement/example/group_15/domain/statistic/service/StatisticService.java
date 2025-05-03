package storemanagement.example.group_15.domain.statistic.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storemanagement.example.group_15.app.dto.response.product.ProductInCartResponseDTO;
import storemanagement.example.group_15.app.dto.response.user.UserResponseDTO;
import storemanagement.example.group_15.domain.orders.entity.OrderEntity;
import storemanagement.example.group_15.domain.orders.repository.OrderRepository;
import storemanagement.example.group_15.domain.statistic.dto.RevenueResponseDTO;
import storemanagement.example.group_15.domain.statistic.dto.UserStatisticResponse;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.helper.JsonHelper;

@Service
public class StatisticService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private JsonHelper jsonHelper;

  @Autowired
  private AuthRepository authRepository;

  public RevenueResponseDTO getStatisticRevenue(
      LocalDate startDate,
      LocalDate endDate) {
    if (startDate == null) {
      startDate = LocalDate.of(2000, 1, 1);
    }
    if (endDate == null) {
      endDate = LocalDate.now();
    }

    LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59

    BigDecimal totalRevenue = orderRepository.sumTotalPaymentBetween(startDateTime, endDateTime);
    Long totalOrder = orderRepository.countByCreatedAtBetween(startDateTime, endDateTime);

    return new RevenueResponseDTO(
        totalRevenue,
        totalOrder);
  }

  public List<ProductInCartResponseDTO> getStatisticProducts(
      LocalDate startDate,
      LocalDate endDate,
      String sortDirection) {
    if (startDate == null) {
      startDate = LocalDate.of(2000, 1, 1);
    }
    if (endDate == null) {
      endDate = LocalDate.now();
    }
    LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59

    List<String> productsString = orderRepository.findProductsOrderBetween(startDateTime, endDateTime);

    List<ProductInCartResponseDTO> productsInOrder = handleMergeProduct(productsString);

    if (sortDirection.equals("DESC")) {
      productsInOrder.sort((p1, p2) -> p2.getQuantity().compareTo(p1.getQuantity()));
    } else {
      productsInOrder.sort((p1, p2) -> p1.getQuantity().compareTo(p2.getQuantity()));
    }

    return productsInOrder;
  }

  public UserStatisticResponse getStatisticByUser(
      Long userId) {
    AuthEntity auth = authRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    List<OrderEntity> orders = orderRepository.findAllByCustomerId(userId);

    BigDecimal totalRevenue = BigDecimal.ZERO;
    Long totalOrder = 0L;
    List<String> productsString = new ArrayList<>();

    for (OrderEntity order : orders) {
      totalRevenue = totalRevenue.add(order.getTotalPayment());
      totalOrder++;
      productsString.add(order.getProducts());
    }
    List<ProductInCartResponseDTO> productsInOrder = handleMergeProduct(productsString);

    UserResponseDTO user = new UserResponseDTO(
        auth.getId(),
        auth.getEmail(),
        auth.getName(),
        auth.getPhone(),
        auth.getAddress(),
        auth.getIsBuy());

    return new UserStatisticResponse(user, totalRevenue, totalOrder, productsInOrder);
  }

  List<ProductInCartResponseDTO> handleMergeProduct(List<String> productsString) {
    Map<Number, ProductInCartResponseDTO> map = new HashMap<>();
    for (String pString : productsString) {
      List<ProductInCartResponseDTO> productInOrder = jsonHelper.convertJsonArrayToProducts(pString);

      for (ProductInCartResponseDTO p : productInOrder) {
        if (map.containsKey(p.getId())) {
          ProductInCartResponseDTO existingProduct = map.get(p.getId());
          existingProduct.setQuantity(existingProduct.getQuantity() + p.getQuantity());
        } else {
          ProductInCartResponseDTO newProduct = new ProductInCartResponseDTO(p.getId(), p.getName(), p.getDesc(),
              p.getPrice(), p.getQuantity());
          map.put(p.getId(), newProduct);
        }
        System.out.println(p.toString());
      }
    }
    List<ProductInCartResponseDTO> res = new ArrayList<>(map.values());

    return res;
  }
}
