package storemanagement.example.group_15.app.dto.request.favorites;

import lombok.Data;

@Data
public class FavoritesRequestDTO {
    private Long customer_id;
    private Long[] product_id;
}
