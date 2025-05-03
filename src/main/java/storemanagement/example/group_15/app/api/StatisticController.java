package storemanagement.example.group_15.app.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.app.dto.response.product.ProductInCartResponseDTO;
import storemanagement.example.group_15.domain.statistic.dto.RevenueResponseDTO;
import storemanagement.example.group_15.domain.statistic.dto.UserStatisticResponse;
import storemanagement.example.group_15.domain.statistic.service.StatisticService;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
  @Autowired
  private StatisticService statisticService;

  @GetMapping("/revenue/weekly")
  public ResponseEntity<ApiResponse<RevenueResponseDTO>> getRevenueWeekly(HttpServletRequest request) {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(7);

    RevenueResponseDTO revenue = statisticService.getStatisticRevenue(startDate, endDate);
    return ResponseEntity.status(SuccessConstant.OK).body(
        ApiResponse.success(SuccessConstant.SUCCESS, revenue, 200));
  }

  @GetMapping("/revenue/monthly")
  public ResponseEntity<ApiResponse<RevenueResponseDTO>> getRevenueThisMonth(HttpServletRequest request) {
    LocalDate now = LocalDate.now();
    LocalDate startDate = now.withDayOfMonth(1);
    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

    RevenueResponseDTO revenue = statisticService.getStatisticRevenue(startDate, endDate);
    return ResponseEntity.status(SuccessConstant.OK).body(
        ApiResponse.success(SuccessConstant.SUCCESS, revenue, 200));
  }

  @GetMapping("/revenue")
  public ResponseEntity<ApiResponse<RevenueResponseDTO>> getRevenueCustom(HttpServletRequest request,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    RevenueResponseDTO revenue = statisticService.getStatisticRevenue(startDate, endDate);
    return ResponseEntity.status(SuccessConstant.OK).body(
        ApiResponse.success(SuccessConstant.SUCCESS, revenue, 200));
  }

  @GetMapping("/products")
  public ResponseEntity<ApiResponse<List<ProductInCartResponseDTO>>> getTopProduct(HttpServletRequest request,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(required = false) String sortDirection) {

    List<ProductInCartResponseDTO> products = statisticService.getStatisticProducts(startDate, endDate, sortDirection);

    return ResponseEntity.status(SuccessConstant.OK).body(
        ApiResponse.success(SuccessConstant.SUCCESS, products, 200));
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<UserStatisticResponse>> getStatisticUser(HttpServletRequest request,
      @PathVariable Long userId) {

    UserStatisticResponse userStatistic = statisticService.getStatisticByUser(userId);

    return ResponseEntity.status(SuccessConstant.OK).body(
        ApiResponse.success(SuccessConstant.SUCCESS, userStatistic, 200));
  }

}
