package id.ac.ui.cs.advprog.youkosoadmin.models.primary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinalProduct extends Product {
    private int finalPrice;

    public FinalProduct(Product product){
        super(product);
        this.finalPrice = product.finalPrice();
    }
}
