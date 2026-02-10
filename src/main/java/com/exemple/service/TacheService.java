package com.exemple.service;

// Imports nécessaires
import com.exemple.Tache;
import com.exemple.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

/**
 * Couche SERVICE : Contient la logique métier et gère les transactions
 * Responsabilités :
 * - Gérer les transactions (BEGIN, COMMIT, ROLLBACK)
 * - Appeler JPA pour manipuler les données
 * - Gérer les erreurs métier
 */
public class TacheService {

    /**
     * Récupère toutes les tâches de la base de données
     * PAS DE TRANSACTION car c'est juste une lecture (SELECT)
     */
    public List<Tache> getAllTaches() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery("SELECT t FROM Tache t ORDER BY t.id", Tache.class)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    /**
     * Récupère une tâche par son ID
     * PAS DE TRANSACTION (lecture simple)
     */
    public Tache getTacheById(int id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Tache.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Crée une nouvelle tâche en base de données
     * TRANSACTION NÉCESSAIRE car on modifie la base (INSERT)
     */
    public Tache createTache(Tache tache) {
        EntityManager em = JPAUtil.getEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(tache);
            tx.commit();

            return tache;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    /**
     * Met à jour une tâche existante
     * TRANSACTION NÉCESSAIRE (UPDATE)
     */
    public Tache updateTache(int id, Tache tacheModifiee) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Tache tache = em.find(Tache.class, id);

            if (tache == null) {
                tx.rollback();
                return null;
            }

            tache.setDescription(tacheModifiee.getDescription());
            tache.setCompleted(tacheModifiee.isCompleted());

            em.merge(tache);

            tx.commit();

            return tache;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    /**
     * Supprime une tâche
     * TRANSACTION NÉCESSAIRE (DELETE)
     */
    public boolean deleteTache(int id) {
        // 1. Créer EntityManager et Transaction
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Tache tache = em.find(Tache.class, id);

            if (tache == null) {
                tx.rollback();
                return false;
            }

            em.remove(tache);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    /**
     * Marque plusieurs tâches comme complétées
     * EXEMPLE de transaction avec plusieurs opérations
     */
    public void marquerTachesCommeCompletes(List<Integer> ids) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            for (Integer id : ids) {
                Tache tache = em.find(Tache.class, id);

                if (tache != null) {
                    tache.setCompleted(true);
                    em.merge(tache);
                }
            }

            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Initialise la base avec des données de test
     */
    public void initialiserDonneesTest() {
        createTache(new Tache("Configurer IntelliJ"));
        createTache(new Tache("Coder l'exercice"));
        createTache(new Tache("Tester l'application"));
    }
}