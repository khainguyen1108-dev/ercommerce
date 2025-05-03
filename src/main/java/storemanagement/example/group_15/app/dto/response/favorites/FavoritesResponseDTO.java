package storemanagement.example.group_15.app.dto.response.favorites;

import lombok.Data;

import java.util.List;

@Data
public class FavoritesResponseDTO {
    private CustomerDTO customer;
    private List<ProductDTO> products;
}


