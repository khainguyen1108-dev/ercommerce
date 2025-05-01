package storemanagement.example.group_15.app.dto.request.events;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventCreateDTO {
  private String name;
  private LocalDateTime timeAt;
  private Long voucherId;
}
