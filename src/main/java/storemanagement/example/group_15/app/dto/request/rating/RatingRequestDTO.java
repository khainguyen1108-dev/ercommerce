package storemanagement.example.group_15.app.dto.request.rating;

import lombok.Data;

@Data
public class RatingRequestDTO {
    private Long productId;
    private Integer stars;
    private String description;
}
