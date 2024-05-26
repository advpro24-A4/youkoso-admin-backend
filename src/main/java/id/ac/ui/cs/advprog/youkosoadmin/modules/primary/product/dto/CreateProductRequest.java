package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProductRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private int price;

    @JsonProperty("stock")
    private int stock;

    @JsonProperty("discount")
    private Integer discount;

    @JsonProperty("image_url")
    private String imageUrl;
}
