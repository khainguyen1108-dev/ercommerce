package storemanagement.example.group_15.app.dto.response.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventResponseDTO {
  private Long id;
  private String name;
  private LocalDateTime timeAt;
  private Long voucherId;
}
