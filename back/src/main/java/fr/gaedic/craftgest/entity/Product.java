package fr.gaedic.craftgest.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String codeRef;

    @Column
    private String description;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column (precision = 10, scale = 2)
    private BigDecimal costMaterial;

    @Column
    private String size;

    @Column (nullable = false)
    private Integer stock;

    @Column (nullable = false)
    private Boolean isVisibleSite;

    @Column (nullable = false)
    private Boolean archive;

    @JoinColumn (name="user_id", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn (name="type_id", nullable = false)
    @ManyToOne
    private ProductType ProductType;

    @JoinColumn (name="collection_id", nullable = false)
    @ManyToOne
    private ProductCollection ProductCollection;

    @JoinColumn (name="finding_id", nullable = false)
    @ManyToOne
    private Finding Finding;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Product() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCostMaterial() {
        return costMaterial;
    }

    public void setCostMaterial(BigDecimal costMaterial) {
        this.costMaterial = costMaterial;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getVisibleSite() {
        return isVisibleSite;
    }

    public void setVisibleSite(Boolean visibleSite) {
        isVisibleSite = visibleSite;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProductType getProductType() {
        return ProductType;
    }

    public void setProductType(ProductType productType) {
        ProductType = productType;
    }

    public ProductCollection getProductCollection() {
        return ProductCollection;
    }

    public void setProductCollection(ProductCollection productCollection) {
        ProductCollection = productCollection;
    }

    public Finding getFinding() {
        return Finding;
    }

    public void setFinding(Finding finding) {
        Finding = finding;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
