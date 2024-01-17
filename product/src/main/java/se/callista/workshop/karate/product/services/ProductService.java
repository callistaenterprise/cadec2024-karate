package se.callista.workshop.karate.product.services;

import java.util.List;
import se.callista.workshop.karate.product.model.ProductValue;

public interface ProductService {

    List<ProductValue> getProducts();

    ProductValue getProduct(String articleId);

    ProductValue createProduct(ProductValue product);

    ProductValue updateProduct(ProductValue product);

    void deleteProduct(String articleId);

}
