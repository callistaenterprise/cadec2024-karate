package se.callista.workshop.karate.inventory.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.callista.workshop.karate.inventory.domain.entity.Inventory;
import se.callista.workshop.karate.inventory.model.InventoryValue;
import se.callista.workshop.karate.inventory.model.InventoryValueMapper;
import se.callista.workshop.karate.inventory.repository.InventoryRepository;

@RequiredArgsConstructor
@Component
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public InventoryValue getInventory(String articleId) {
        return inventoryRepository
            .findByarticleId(articleId)
            .map(InventoryValueMapper::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Inventory with articleId " + articleId + " not found"));
    }

    @JmsListener(destination = "stock", containerFactory = "containerFactory")
    public void receivestockMessage(String inventoryMessage) {
        InventoryValue inventoryValue = unmarshall(inventoryMessage);
        Inventory inventory =
            inventoryRepository
                .findByarticleId(inventoryValue.getArticleId())
                .orElse(Inventory
                    .builder()
                    .articleId(inventoryValue.getArticleId())
                    .stock(0L)
                    .build());
        if (inventoryValue.getStock() < 0) {
            inventoryRepository.delete(inventory);
        } else {
            inventory.setStock(inventory.getStock() + inventoryValue.getStock());
            inventoryRepository.save(inventory);
            String stockMessage = marshall(InventoryValueMapper.fromEntity(inventory));
            jmsTemplate.convertAndSend("level", stockMessage);
        }
    }

    @SneakyThrows
    private InventoryValue unmarshall(String inventoryMessage)  {
        return objectMapper.readValue(inventoryMessage, InventoryValue.class);
    }

    @SneakyThrows
    private String marshall(InventoryValue inventoryValue) {
        return objectMapper.writeValueAsString(inventoryValue);
    }

}
