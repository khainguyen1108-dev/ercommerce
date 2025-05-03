package storemanagement.example.group_15.app.dto.response.rating;

import lombok.Data;
import storemanagement.example.group_15.app.dto.response.favorites.CustomerDTO;
import storemanagement.example.group_15.app.dto.response.favorites.ProductDTO;

import java.util.List;

@Data
public class RatingResponseDTO {
    private Long id;
    private CustomerDTO customer;
    private ProductDTO products;
    private Integer stars;
    private String description;
}
