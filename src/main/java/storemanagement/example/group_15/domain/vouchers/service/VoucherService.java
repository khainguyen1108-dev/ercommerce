package storemanagement.example.group_15.domain.vouchers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import storemanagement.example.group_15.app.dto.request.voucher.VoucherRequestDTO;
import storemanagement.example.group_15.domain.vouchers.dto.VoucherResponseDTO;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import storemanagement.example.group_15.domain.vouchers.repository.VoucherRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.DateHelper;
import storemanagement.example.group_15.infrastructure.helper.JsonHelper;

import java.util.*;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
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
}
