package se.callista.workshop.karate.product.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq")
    protected Long productId;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "int default 0")
    protected Integer version;

    @Column(name = "name", length = 255, nullable = false)
    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @JsonProperty("articleId")
    private String articleId;

    @Builder
    public Product(Long productId, String name, String articleId, Integer version) {
        this.productId = productId;
        this.name = name;
        this.articleId = articleId;
        this.version = version;
    }
}
