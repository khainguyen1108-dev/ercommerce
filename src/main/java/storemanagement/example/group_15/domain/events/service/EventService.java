package storemanagement.example.group_15.domain.events.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import storemanagement.example.group_15.app.dto.request.events.EventCreateDTO;
import storemanagement.example.group_15.app.dto.response.events.EventResponseDTO;
import storemanagement.example.group_15.domain.events.entity.EventEntity;
import storemanagement.example.group_15.domain.events.repository.EventRepository;
import storemanagement.example.group_15.domain.vouchers.entity.VoucherEntity;
import storemanagement.example.group_15.domain.vouchers.repository.VoucherRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

@Service
public class EventService {
  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private VoucherRepository voucherRepository;

  public EventResponseDTO create(EventCreateDTO input) {
    VoucherEntity voucher = null;
    if (input.getVoucherId() != null) {
      voucher = voucherRepository.findById(input.getVoucherId())
          .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Voucher not found"));
    }

    EventEntity event = EventEntity.builder()
        .name(input.getName())
        .timeAt(input.getTimeAt())
        .voucher(voucher)
        .build();

    this.eventRepository.save(event);

    return convertToDTO(event);
  }

  public List<EventResponseDTO> getAll() {
    return this.eventRepository.findAll().stream().map(this::convertToDTO).toList();
  }

  public EventResponseDTO getById(Long id) {
    EventEntity event = this.eventRepository.findById(id)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Can not find event with id " + id));
    return convertToDTO(event);
  }

  public EventResponseDTO update(Long id, EventCreateDTO input) {
    // EventEntity event = this.eventRepository
    Optional<EventEntity> event = this.eventRepository.findById(id);
    if (event.isPresent()) {
      VoucherEntity voucher = null;
      if (input.getVoucherId() != null) {
        voucher = voucherRepository.findById(input.getVoucherId())
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Voucher not found"));
      }

      EventEntity eventUpdate = event.get();
      eventUpdate.setName(input.getName());
      eventUpdate.setTimeAt(input.getTimeAt());
      eventUpdate.setVoucher(voucher);

      this.eventRepository.save(eventUpdate);
      return convertToDTO(eventUpdate);
    } else {
      throw new AppException(HttpStatus.NOT_FOUND, "Event with id " + id + " not found.");
    }
  }

  public void delete(Long id) {
    this.eventRepository.findById(id)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Can not find event with id " + id));

    this.eventRepository.deleteById(id);
  }

  private EventResponseDTO convertToDTO(EventEntity event) {
    return new EventResponseDTO(
        event.getId(),
        event.getName(),
        event.getTimeAt(),
        event.getVoucher() != null ? event.getVoucher().getId() : null);
  }
}
