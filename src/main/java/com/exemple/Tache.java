package com.exemple;

// Imports des annotations JPA
import jakarta.persistence.*;

/**
 * Classe représentant une tâche (entité JPA)
 * Sera automatiquement mappée vers une table en base de données
 */
@Entity
@Table(name = "taches")
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "completed")
    private boolean completed = false;

    public Tache() {}

    public Tache(String description) {
        this.description = description;
    }

    public Tache(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Retourne l'identifiant de la tâche
     */
    public Integer getId() {
        return id;
    }

    /**
     * Définit l'identifiant (rarement utilisé car auto-généré)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retourne la description de la tâche
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description de la tâche
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indique si la tâche est complétée
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Marque la tâche comme complétée ou non
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Retourne une représentation textuelle de la tâche
     * Utile pour le débogage (System.out.println)
     */
    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}