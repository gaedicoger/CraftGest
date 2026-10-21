package fr.gaedic.craftgest.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private Integer product_id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
