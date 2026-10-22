package fr.gaedic.craftgest.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Integer id;

    private String name;

    private String codeRef;

    private String description;

    private BigDecimal price;

    private BigDecimal costMaterial;

    private String size;

    private Integer stock;

    private Boolean isVisibleSite;

    private Boolean archive;

    private Integer userId;

    private Integer typeId;

    private Integer collectionId;

    private Integer findingId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //Constructeur vide:
    public Product() {}

    //Getters et setters:
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getFindingId() {
        return findingId;
    }

    public void setFindingId(Integer findingId) {
        this.findingId = findingId;
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
