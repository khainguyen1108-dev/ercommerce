package storemanagement.example.group_15.domain.vouchers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import storemanagement.example.group_15.app.dto.request.voucher.VoucherRequestDTO;
import storemanagement.example.group_15.domain.carts.entity.CartEntity;
import storemanagement.example.group_15.domain.carts.repository.CartRepository;
import storemanagement.example.group_15.domain.collections.entity.CollectionEntity;
import storemanagement.example.group_15.domain.collections.repository.CollectionRepository;
import storemanagement.example.group_15.domain.events.entity.EventEntity;
import storemanagement.example.group_15.domain.events.repository.EventRepository;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.entity.ProductInCartEntity;
import storemanagement.example.group_15.domain.vouchers.constant.VoucherType;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherDTO;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherResponseDTO;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import storemanagement.example.group_15.domain.vouchers.repository.VoucherRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.DateHelper;
import storemanagement.example.group_15.infrastructure.helper.JsonHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoucherService {
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private JsonHelper jsonHelper;
    public VoucherResponseDTO create(VoucherRequestDTO input) throws JsonProcessingException {

        VoucherEntity voucher = new VoucherEntity();
        voucher.setType(input.getType());
        voucher.setCondition(this.jsonHelper.setConditionsAsList(input.getCondition()));
        voucher.setDiscountValue(input.getDiscountValue());
        voucher.setStartDate(DateHelper.normalizeDate(input.getStartDate()));
        voucher.setEndDate(DateHelper.normalizeDate(input.getEndDate()));
        voucher.setName(input.getName());
        VoucherEntity output = this.voucherRepository.save(voucher);
        VoucherResponseDTO out = new VoucherResponseDTO();
        out.setName(output.getName());
        out.setType(output.getType());
        out.setStartDate(output.getStartDate());
        out.setEndDate(output.getEndDate());
        out.setId(output.getId());
        out.setDiscountValue(output.getDiscountValue());
        out.setConditions(this.jsonHelper.getConditionsAsList(output.getCondition()));
        return out;
    }
    public VoucherResponseDTO getById(Long id) {
        Optional<VoucherEntity> output = this.voucherRepository.findById(id);
        if (output.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "voucher_id.invalid");
        }
        VoucherResponseDTO out = new VoucherResponseDTO();
        out.setName(output.get().getName());
        out.setType(output.get().getType());
        out.setStartDate(output.get().getStartDate());
        out.setEndDate(output.get().getEndDate());
        out.setDiscountValue(output.get().getDiscountValue());
        out.setId(output.get().getId());
        out.setConditions(this.jsonHelper.getConditionsAsList(output.get().getCondition()));
        return out;
    }
    public Map<String, Long> delete(Long id){
        Optional<VoucherEntity> output = this.voucherRepository.findById(id);
        if (output.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "voucher_id.invalid");
        }
        this.voucherRepository.delete(output.get());
        Map<String, Long> response = new HashMap<>();
        response.put("id", id);
        return response;
    }
    public Long update(Long id, VoucherRequestDTO input){
        Optional<VoucherEntity> output = this.voucherRepository.findById(id);
        if (output.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "voucher_id.not_found");
        }
        if (input.getCondition() != null && !input.getCondition().isEmpty()){
            output.get().setCondition(this.jsonHelper.setConditionsAsList(input.getCondition()));
        }
        if (input.getType() != null){
            output.get().setType(input.getType());
        }
        if (input.getName() != null && !input.getName().isBlank()){
            output.get().setName(input.getName());
        }
        if (input.getStartDate() != null && !input.getStartDate().isBlank()){
            output.get().setStartDate(DateHelper.normalizeDate(input.getStartDate()));

        }
        if (input.getEndDate() != null && !input.getEndDate().isBlank()){
            output.get().setEndDate(DateHelper.normalizeDate(input.getEndDate()));
        }
        if (input.getDiscountValue() != null){
            output.get().setDiscountValue(input.getDiscountValue());
        }
        this.voucherRepository.save(output.get());
        return id;
    }
    public VoucherDTO toDTO(VoucherEntity entity) {
        return VoucherDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .discountValue(entity.getDiscountValue())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .type(entity.getType())
                .condition(this.jsonHelper.getConditionsAsList(entity.getCondition()))
                .eventId(entity.getEvent() != null ? entity.getEvent().getId() : null)
                .build();
    }

    public List<VoucherDTO>  getAll(){
        List<VoucherEntity> input = this.voucherRepository.findAll();
        List<VoucherDTO> response = input.stream().map(this::toDTO).toList();

        return response;
    }
    private void applyVoucherToCart(CartEntity cart, VoucherEntity voucher, BigDecimal discount) {
        cart.setVoucher(voucher);
        cart.setTotalPayment(cart.getTotalPrice().subtract(discount));
        this.cartRepository.save(cart);
    }
    public String calcPrice(Long id, Long customer_id) {
        CartEntity cart = this.cartRepository.findByCustomerId(customer_id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + customer_id));

        VoucherEntity voucher = this.voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "voucher not found: " + id));

        LocalDate today = LocalDate.now();
        if (today.isBefore(voucher.getStartDate()) || today.isAfter(voucher.getEndDate())) {
            throw new AppException(HttpStatus.NOT_FOUND, "voucher overdue: " + id);
        }

        List<ProductInCartEntity> listProduct = cart.getProductInCartEntities();
        BigDecimal totalPrice = cart.getTotalPrice();
        BigDecimal discountRate = BigDecimal.valueOf(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100));
        BigDecimal discount;

        switch (voucher.getType()) {
            case COLLECTION -> {
                List<ProductEntity> matchedProducts = new ArrayList<>();
                for (String collection_id : jsonHelper.getConditionsAsList(voucher.getCondition())) {
                    CollectionEntity collection = collectionRepository.findById(Long.parseLong(collection_id))
                            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "collection not found: " + collection_id));
                    matchedProducts.addAll(collection.getProducts());
                }
                for (ProductInCartEntity item : listProduct) {
                    if (matchedProducts.contains(item.getProduct())) {
                        discount = totalPrice.multiply(discountRate);
                        applyVoucherToCart(cart, voucher, discount);
                        return "success";
                    }
                }
                return "cart cannot apply voucher";
            }

            case PRODUCT -> {
                List<String> condition = jsonHelper.getConditionsAsList(voucher.getCondition());
                for (ProductInCartEntity item : listProduct) {
                    if (condition.contains(item.getProduct().getId().toString())) {
                        discount = totalPrice.multiply(discountRate);
                        applyVoucherToCart(cart, voucher, discount);
                        return "success";
                    }
                }
                return "cart cannot apply voucher";
            }

            case EVENT -> {
                Long event_id = Long.parseLong(jsonHelper.getConditionsAsList(voucher.getCondition()).getFirst());
                Optional<EventEntity> eventEntity = this.eventRepository.findById(event_id);
                if (eventEntity.isEmpty()){
                    throw new AppException(HttpStatus.BAD_REQUEST," event_id.not_found");
                }
                if (!eventEntity.get().getTimeAt().toLocalDate().equals(today)) {
                    return "cannot apply voucher at today";
                }
                discount = totalPrice.multiply(discountRate);
                applyVoucherToCart(cart, voucher, discount);
                return "success";
            }

            case ALL -> {
                discount = totalPrice.multiply(discountRate);
                applyVoucherToCart(cart, voucher, discount);
                return "success";
            }

            default -> throw new AppException(HttpStatus.BAD_REQUEST, "type.not_found");
        }
    }














}
