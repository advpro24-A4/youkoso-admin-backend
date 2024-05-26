package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.UnauthorizedException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.FinalProduct;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Product;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product.dto.CreateProductRequest;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product.dto.DeleteRequest;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product.dto.EditProductRequest;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthService;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;

    private final AuthService authService;

    @Autowired
    public ProductController(IProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @GetMapping("")
    public ResponseEntity<DefaultResponse<Page<FinalProduct>>> getProducts(
            @RequestParam(value = "search", required = false) String search,
            Pageable pageable
    ){
        if(search != null){
            Page<FinalProduct> products = productService.searchProduct(search, pageable);
            DefaultResponse<Page<FinalProduct>> response = new DefaultResponseBuilder<Page<FinalProduct>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get products")
                    .data(products)
                    .build();
            return ResponseEntity.ok(response);
        }

        Page<FinalProduct> products = productService.getProducts(pageable);
        DefaultResponse<Page<FinalProduct>> response = new DefaultResponseBuilder<Page<FinalProduct>>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success get products")
                .data(products)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<DefaultResponse<FinalProduct>> createProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CreateProductRequest request
            ){

        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        if(request.getPrice() <= 0 || request.getStock() < 0 || request.getDiscount() < 0 || request.getDiscount() > 100){
            throw new IllegalArgumentException("Invalid input");
        }

        Product product = getProduct(request.getName(), request.getDescription(), request.getPrice(), request.getStock(), request.getDiscount(), request.getImageUrl(), 0);
        FinalProduct finalProduct = productService.createProduct(product);

        DefaultResponse<FinalProduct> response = new DefaultResponseBuilder<FinalProduct>()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Success create product")
                .data(finalProduct)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("")
    public ResponseEntity<DefaultResponse<FinalProduct>> editProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody EditProductRequest request
            ){

        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if(request.getPrice() <= 0 || request.getStock() < 0 || request.getDiscount() < 0 || request.getDiscount() > 100){
            throw new IllegalArgumentException("Invalid input");
        }

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Product product = getProduct(request.getName(), request.getDescription(), request.getPrice(), request.getStock(), request.getDiscount(), request.getImageUrl(), request.getId());
        FinalProduct finalProduct = productService.editProduct(product);

        DefaultResponse<FinalProduct> response = new DefaultResponseBuilder<FinalProduct>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success edit product")
                .data(finalProduct)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<DefaultResponse<FinalProduct>> deleteProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody DeleteRequest request
            ){
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        FinalProduct finalProduct = productService.deleteProduct(request.getId());

        DefaultResponse<FinalProduct> response = new DefaultResponseBuilder<FinalProduct>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success delete product")
                .data(finalProduct)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<FinalProduct>> getProduct(@PathVariable int id){
        FinalProduct finalProduct = productService.getProduct(id);

        DefaultResponse<FinalProduct> response = new DefaultResponseBuilder<FinalProduct>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success get product")
                .data(finalProduct)
                .build();

        return ResponseEntity.ok(response);
    }

    private Product getProduct(String name, String description, int price, int stock, Integer discount, String imageUrl, Integer id) {
        Product product = new Product();
        product.setId(id);
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPrice(price);
        product.setProductStock(stock);
        product.setProductDiscount(discount);
        product.setProductImage(imageUrl);
        return product;
    }
}
