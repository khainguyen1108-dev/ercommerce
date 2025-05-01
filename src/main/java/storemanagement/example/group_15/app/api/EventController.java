package storemanagement.example.group_15.app.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.events.EventCreateDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.app.dto.response.events.EventResponseDTO;
import storemanagement.example.group_15.domain.events.service.EventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController()
@RequestMapping("/events")
public class EventController {
  private static final Logger log = LoggerFactory.getLogger(EventController.class);

  @Autowired
  private EventService eventService;

  @PostMapping()
  public ResponseEntity<ApiResponse<EventResponseDTO>> create(@RequestBody @Valid EventCreateDTO entity) {
    try {
      EventResponseDTO output = this.eventService.create(entity);
      return ResponseEntity.status(SuccessConstant.CREATED)
          .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
    } catch (Exception e) {
      log.error("ERROR WHEN createEvent", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getAll() {
    try {
      List<EventResponseDTO> events = this.eventService.getAll();
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, events, 200));
    } catch (Exception e) {
      log.error("ERROR WHEN getEvents", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<EventResponseDTO>> getById(@PathVariable Long id) {
    try {
      EventResponseDTO event = this.eventService.getById(id);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, event, 200));
    } catch (Exception e) {
      log.error("ERROR WHEN getEventsById", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<EventResponseDTO>> updateById(@PathVariable Long id,
      @RequestBody @Valid EventCreateDTO entity) {
    try {
      EventResponseDTO event = this.eventService.update(id, entity);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, event, 200));
    } catch (Exception e) {
      log.error("ERROR WHEN updateById", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
    try {
      this.eventService.delete(id);
      return ResponseEntity.ok()
          .body(ApiResponse.success(SuccessConstant.SUCCESS, null, 200));
    } catch (Exception e) {
      log.error("ERROR WHEN deleteEvents", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
