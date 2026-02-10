package com.exemple.util;

// Import des classes JPA nécessaires
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitaire pour gérer la création des EntityManager
 * Pattern Singleton pour n'avoir qu'une seule EntityManagerFactory
 */
public class JPAUtil {
    private static final EntityManagerFactory emf;

    static {
        try {

            emf = Persistence.createEntityManagerFactory("todosPU");

        } catch (Throwable ex) {

            System.err.println("Erreur lors de la création de l'EntityManagerFactory : " + ex);

            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Crée et retourne un nouveau EntityManager
     * EntityManager = gestionnaire qui permet de faire des opérations sur la base
     * À utiliser pour CHAQUE opération, puis fermer avec em.close()
     */
    public static EntityManager getEntityManager() {

        return emf.createEntityManager();
    }

    /**
     * Ferme l'EntityManagerFactory
     * À appeler lors de l'arrêt de l'application pour libérer les ressources
     */
    public static void close() {

        if (emf != null && emf.isOpen()) {

            emf.close();
        }
    }
}