package fr.gaedic.craftgest.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Entité JPA : cette classe représente la table "users".
// Hibernate s'en sert pour lire et écrire les utilisateurs en base.
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String pseudo;
    @Column(nullable = false, unique=true )
    private String mail;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private String business;
    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Boolean mailVerified;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //Constructeur : Permet de créer un objet user.
    // JPA l'utilise pour créer un ojbet user pour chaque ligne de la table
    public User() {}

    //Getters et setters : Les attribuent de ma classe sont privés, ça permet d'avoir des accès contrôlés
    // pour lire ou modifier les attributs depuis l'extérieur de la classe.

    //Getters: Permet de lire un attribut depuis l'extérieur de la classe
    public Integer getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMail() {
        return mail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getBusiness() {
        return business;
    }

    public String getSlug() {
        return slug;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getMailVerified() {
        return mailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    //Setters: Modifient un attribut depuis l'exterieur de la class.
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMailVerified(Boolean mailVerified) {
        this.mailVerified = mailVerified;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
