package com.exemple;

// Imports JAX-RS
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuration de l'application REST
 * Point d'entrée pour JAX-RS
 */
@ApplicationPath("/api")
public class RestConfig extends Application {}