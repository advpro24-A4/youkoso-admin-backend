package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.FinalProduct;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    FinalProduct createProduct(Product product);
    FinalProduct editProduct(Product product);
    FinalProduct deleteProduct(int product);
    FinalProduct getProduct(int id);
    Page<FinalProduct> getProducts(Pageable productPageable);
    Page<FinalProduct> searchProduct(String productName, Pageable productPageable);
}
