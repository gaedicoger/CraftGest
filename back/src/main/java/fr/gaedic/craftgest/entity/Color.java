package fr.gaedic.craftgest.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Entité JPA : cette classe représente la table "colors".
// Hibernate s'en sert pour lire et écrire les couleurs en base.
@Entity
@Table(name="colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    //Attention ici on change d'annotation pour les clés étrangères.
    // On fait une "jointure" on rappelle le nom de la colonne de la table
    //Puis on indique l'annotation many to one, plusieurs couleurs pour un utilisateur
    @JoinColumn(name="user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //Constructeur : Permet de créer un objet color.
    // JPA l'utilise pour créer un objet color pour chaque ligne de la table
    public Color() {}

    //Getters et setters : Les attribuent de ma classe sont privés, ça permet d'avoir des accès contrôlés
    // pour lire ou modifier les attributs depuis l'extérieur de la classe.

    //Getters: Permet de lire un attribut depuis l'extérieur de la classe
    public Integer getId() {
        return id;
    }

    //Setters: Modifient un attribut depuis l'exterieur de la class.
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
