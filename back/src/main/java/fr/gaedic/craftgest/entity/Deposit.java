package fr.gaedic.craftgest.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="deposits",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "product_id"}))
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name="event_id", nullable = false)
    @ManyToOne
    private Event event;

    @JoinColumn(name="product_id", nullable = false)
    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private Integer quantityDeposited;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Deposit() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
