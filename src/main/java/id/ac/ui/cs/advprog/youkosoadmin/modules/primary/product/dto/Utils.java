package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product.dto;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Product;

public class Utils {
    public static Product convertToProduct(CreateProductRequest request) {
        Product product = new Product();
        return getProduct(product, request.getName(), request.getDescription(), request.getPrice(), request.getStock(), request.getDiscount(), request.getImageUrl());
    }

    public static Product convertToProduct(EditProductRequest request) {
        Product product = new Product();
        product.setId(request.getId());
        return getProduct(product, request.getName(), request.getDescription(), request.getPrice(), request.getStock(), request.getDiscount(), request.getImageUrl());
    }

    private static Product getProduct(Product product, String name, String description, int price, int stock, Integer discount, String imageUrl) {
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPrice(price);
        product.setProductStock(stock);
        product.setProductDiscount(discount);
        product.setProductImage(imageUrl);
        return product;
    }
}
