package fr.gaedic.craftgest.model;

import java.time.LocalDateTime;

// Modèle : cette classe représente la table "colors".
// Le repository (JDBC) la remplit avec les lignes lues en base.
public class Color {
    private Integer id;

    private String name;
    // Clé étrangère : on stocke seulement l'id de l'utilisateur (colonne user_id)
    private Integer userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //Constructeur : Permet de créer un objet color.
    // Le repository l'utilisera pour créer un objet color pour chaque ligne lue en base
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
