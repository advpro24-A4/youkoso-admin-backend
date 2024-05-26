package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.FinalProduct;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    private ProductServiceImpl productService;

    private Product product;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setProductDescription("Test Description");
        product.setProductPrice(1000);
        product.setProductStock(10);
        product.setProductDiscount(0);
    }


    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }


    @Test
    void createProductReturnsFinalProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        FinalProduct result = productService.createProduct(product);

        assertEquals(product.getId(), result.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void editProductReturnsFinalProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        FinalProduct result = productService.editProduct(product);

        assertEquals(product.getId(), result.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProductReturnsFinalProductWhenFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        FinalProduct result = productService.deleteProduct(1);

        assertEquals(product.getId(), result.getId());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProductThrowsNotFoundExceptionWhenNotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1));
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void getProductReturnsFinalProductWhenFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        FinalProduct result = productService.getProduct(1);

        assertEquals(product.getId(), result.getId());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductThrowsNotFoundExceptionWhenNotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProduct(1));
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductsReturnsPageOfFinalProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAllBy(pageable)).thenReturn(productPage);

        Page<FinalProduct> result = productService.getProducts(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(product.getId(), result.getContent().getFirst().getId());
        verify(productRepository, times(1)).findAllBy(pageable);
    }

    @Test
    void searchProductReturnsPageOfFinalProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findByProductNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(productPage);

        Page<FinalProduct> result = productService.searchProduct("Test", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(product.getId(), result.getContent().getFirst().getId());
        verify(productRepository, times(1)).findByProductNameContainingIgnoreCase("Test", pageable);
    }
}
