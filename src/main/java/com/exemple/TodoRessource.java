package com.exemple;

// Imports JAX-RS et service
import com.exemple.security.Secured;
import com.exemple.service.TacheService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Ressource REST pour gérer les tâches (CRUD)
 * Couche REST : Gère les requêtes/réponses HTTP
 */
@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoRessource {

    private final TacheService tacheService = new TacheService();

    /**
     * GET /api/todos
     * Récupère toutes les tâches
     */
    @GET
    @Secured
    public List<Tache> getTasks() {
        return tacheService.getAllTaches();
    }

    /**
     * GET /api/todos/{id}
     * Récupère une tâche spécifique par son ID
     * Exemple : GET /api/todos/1
     */
    @GET
    @Path("/{id}")
    public Response getTask(@PathParam("id") int id) {
        Tache tache = tacheService.getTacheById(id);

        if (tache == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tâche non trouvée")
                    .build();
        }

        return Response.ok(tache).build();
    }

    /**
     * POST /api/todos
     * Crée une nouvelle tâche
     * Body de la requête : {"description":"Ma tâche"}
     */
    @POST
    public Response addTask(Tache tache) {

        try {
            Tache created = tacheService.createTache(tache);
            return Response.status(Response.Status.CREATED)
                    .entity(created)
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la création : " + e.getMessage())
                    .build();
        }
    }

    /**
     * PUT /api/todos/{id}
     * Met à jour une tâche existante
     * Exemple : PUT /api/todos/1
     * Body : {"description":"Nouvelle description","completed":true}
     */
    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") int id, Tache tache) {

        try {
            Tache updated = tacheService.updateTache(id, tache);
            if (updated == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tâche non trouvée")
                        .build();
            }

            return Response.ok(updated).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour : " + e.getMessage())
                    .build();
        }
    }

    /**
     * DELETE /api/todos/{id}
     * Supprime une tâche
     * Exemple : DELETE /api/todos/1
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") int id) {
        try {
            boolean deleted = tacheService.deleteTache(id);

            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tâche non trouvée")
                        .build();
            }

            return Response.noContent().build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la suppression : " + e.getMessage())
                    .build();
        }
    }

    /**
     * POST /api/todos/complete-multiple
     * Marque plusieurs tâches comme complètes en une seule transaction
     * Body : [1, 2, 3]
     */
    @POST
    @Path("/complete-multiple")
    public Response completeMultiple(List<Integer> ids) {

        try {
            tacheService.marquerTachesCommeCompletes(ids);

            return Response.ok()
                    .entity("Tâches marquées comme complètes avec succès")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la complétion : " + e.getMessage())
                    .build();
        }
    }

    /**
     * POST /api/todos/init
     * Initialise la base avec des données de test
     */
    @POST
    @Path("/init")
    public Response initData() {
        try {
            tacheService.initialiserDonneesTest();

            return Response.ok()
                    .entity("Données de test initialisées")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de l'initialisation : " + e.getMessage())
                    .build();
        }
    }
}