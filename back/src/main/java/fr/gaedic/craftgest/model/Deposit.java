package fr.gaedic.craftgest.model;

import java.time.LocalDateTime;

public class Deposit {
    private Integer id;

    private Integer eventId;

    private Integer productId;

    private Integer quantityDeposited;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Deposit() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantityDeposited() {
        return quantityDeposited;
    }

    public void setQuantityDeposited(Integer quantityDeposited) {
        this.quantityDeposited = quantityDeposited;
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
