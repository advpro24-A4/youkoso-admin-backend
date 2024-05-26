package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.FinalProduct;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public FinalProduct createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return new FinalProduct(savedProduct);
    }

    @Override
    public FinalProduct editProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return new FinalProduct(savedProduct);
    }

    @Override
    public FinalProduct deleteProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
        return new FinalProduct(product);
    }

    @Override
    public FinalProduct getProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        return new FinalProduct(product);
    }

    @Override
    public Page<FinalProduct> getProducts(Pageable productPageable) {
        Page<Product> products = productRepository.findAllBy(productPageable);
        return products.map(FinalProduct::new);
    }

    @Override
    public Page<FinalProduct> searchProduct(String productName, Pageable productPageable) {
        Page<Product> products = productRepository.findByProductNameContainingIgnoreCase(productName, productPageable);
        return products.map(FinalProduct::new);
    }
}

