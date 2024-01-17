package se.callista.workshop.karate.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import se.callista.workshop.karate.inventory.model.InventoryValue;
import se.callista.workshop.karate.product.domain.entity.Product;
import se.callista.workshop.karate.product.model.ProductValue;
import se.callista.workshop.karate.product.repository.ProductRepository;

@RequiredArgsConstructor
@Component
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<ProductValue> getProducts() {
        return productRepository.findAllByOrderByProductId().stream()
            .map(product -> {
                long stock;
                try {
                    InventoryValue inventoryValue = inventoryService.getInventory(product.getArticleId());
                    stock = inventoryValue.getStock();
                } catch (RuntimeException e) {
                    stock = 0;
                }
                return ProductValue.fromEntity(product, stock);
            })
            .toList();
    }

    @Override
    public ProductValue getProduct(String articleId) {
        Product product = productRepository.findByarticleId(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Product " + articleId + " not found"));
        long stock;
        try {
            InventoryValue inventoryValue = inventoryService.getInventory(product.getArticleId());
            stock = inventoryValue.getStock();
        } catch (RuntimeException e) {
            stock = 0;
        }
        return ProductValue.fromEntity(product, stock);
    }

    @Override
    public ProductValue createProduct(ProductValue productValue) {
        Product product = ProductValue.fromValue(productValue);
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException(productValue.getArticleId() + " already exists");
        }
        InventoryValue inventoryValue =
            InventoryValue
                .builder()
                .articleId(product.getArticleId())
                .stock(0L)
                .build();
        jmsTemplate.convertAndSend("stock", marshall(inventoryValue));
        return ProductValue.fromEntity(product, 0L);
    }

    @Override
    public ProductValue updateProduct(ProductValue updatedProduct) {
        Product product = productRepository.findByarticleId(updatedProduct.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException("Product " + updatedProduct.getArticleId() + " not found"));
        product.setName(updatedProduct.getName());
        productRepository.save(product);
        long stock;
        try {
            InventoryValue inventoryValue = inventoryService.getInventory(product.getArticleId());
            stock = inventoryValue.getStock();
        } catch (RuntimeException e) {
            stock = 0;
        }
        return ProductValue.fromEntity(product, stock);
    }

    @Override
    public void deleteProduct(final String articleId) {
        Product product = productRepository.findByarticleId(articleId)
            .orElseThrow(() -> new EntityNotFoundException("Product " + articleId + " not found"));
        productRepository.delete(product);
        InventoryValue inventoryValue =
            InventoryValue
                .builder()
                .articleId(product.getArticleId())
                .stock(-1L)
                .build();
        jmsTemplate.convertAndSend("stock", marshall(inventoryValue));
    }

    @SneakyThrows
    private String marshall(InventoryValue inventoryValue) {
        return objectMapper.writeValueAsString(inventoryValue);
    }

}
