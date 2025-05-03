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
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.products.entity.ProductInCartEntity;
import storemanagement.example.group_15.domain.vouchers.constant.VoucherType;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherResponseDTO;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import storemanagement.example.group_15.domain.vouchers.repository.VoucherRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.DateHelper;
import storemanagement.example.group_15.infrastructure.helper.JsonHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class VoucherService {
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private CartRepository cartRepository;
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
    public String calcPrice(Long id, Long customer_id){
        Optional<CartEntity> cart = this.cartRepository.findByCustomerId(customer_id);
        if (cart.isEmpty()){
           throw  new AppException(HttpStatus.NOT_FOUND, "Cart not found for user: " + customer_id);
        }
        Optional<VoucherEntity> vouchers = this.voucherRepository.findById(id);
        if (vouchers.isEmpty()){
            throw  new AppException(HttpStatus.NOT_FOUND, "voucher not found: " + id);
        }
        LocalDate startDate = vouchers.get().getStartDate();
        LocalDate endDate = vouchers.get().getEndDate();
        LocalDate today = LocalDate.now();

        boolean isTodayBetween = !today.isBefore(startDate) && !today.isAfter(endDate);
        if (!isTodayBetween){
            throw new AppException(HttpStatus.NOT_FOUND, "voucher overdue: " + id);

        }
        long discount;

        List<ProductInCartEntity> listProduct = cart.get().getProductInCartEntities();
        if (vouchers.get().getType() == VoucherType.COLLECTION){
            List<ProductEntity> products = new ArrayList<>();
            List<String> condition = this.jsonHelper.getConditionsAsList(vouchers.get().getCondition());
            for (String collection_id : condition){
                Optional<CollectionEntity> collectionEntity = this.collectionRepository.findById(Long.parseLong(collection_id));
                if (collectionEntity.isEmpty()){
                    throw  new AppException(HttpStatus.NOT_FOUND, "collection not found: " + collection_id);
                }
                List<ProductEntity> productEntities = collectionEntity.get().getProducts();
                products.addAll(productEntities);
            }
            for (ProductInCartEntity productInCartEntity : listProduct){
                ProductEntity product = productInCartEntity.getProduct();
                if (products.contains(product)){
                    discount = Long.parseLong(cart.get().getTotalPrice().toString())*(vouchers.get().getDiscountValue() / 100);
                    cart.get().setVoucher(vouchers.get());
                    cart.get().setTotalPayment(cart.get().getTotalPayment().subtract(BigDecimal.valueOf(discount)));
                    this.cartRepository.save(cart.get());
                    return "success";
                }
            }
            return "cart cannot apply voucher";
        }
        if (vouchers.get().getType() == VoucherType.PRODUCT){
            List<String> condition = this.jsonHelper.getConditionsAsList(vouchers.get().getCondition());
            for (ProductInCartEntity productInCartEntity : listProduct){
                ProductEntity product = productInCartEntity.getProduct();
                if (condition.contains(product.getId().toString())){
                    discount = Long.parseLong(cart.get().getTotalPrice().toString())*(vouchers.get().getDiscountValue() / 100);

                    cart.get().setVoucher(vouchers.get());
                    cart.get().setTotalPayment(cart.get().getTotalPayment().subtract(BigDecimal.valueOf(discount)));
                    this.cartRepository.save(cart.get());
                    return "success";
                }
            }
            return "cart cannot apply voucher";
        }
        if (vouchers.get().getType() == VoucherType.EVENT){
            EventEntity event = vouchers.get().getEvent();
            if (!event.getTimeAt().toLocalDate().equals(LocalDate.now())){
                return "cannot apply voucher at today";
            }
            discount = Long.parseLong(cart.get().getTotalPrice().toString())*(vouchers.get().getDiscountValue() / 100);
            cart.get().setVoucher(vouchers.get());
            cart.get().setTotalPayment(cart.get().getTotalPayment().subtract(BigDecimal.valueOf(discount)));
            this.cartRepository.save(cart.get());
            return "success";
        }
        if (vouchers.get().getType() == VoucherType.ALL){
            discount = Long.parseLong(cart.get().getTotalPrice().toString())*(vouchers.get().getDiscountValue() / 100);
            cart.get().setVoucher(vouchers.get());
            cart.get().setTotalPayment(cart.get().getTotalPayment().subtract(BigDecimal.valueOf(discount)));
            this.cartRepository.save(cart.get());
            return "success";
        }
        throw  new AppException(HttpStatus.BAD_REQUEST, "type.not_found");
    }















}
