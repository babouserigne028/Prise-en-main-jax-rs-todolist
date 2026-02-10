package com.exemple.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token manquant ou invalide")
                            .build()
            );
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        if (!TokenUtil.isValid(token)) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token invalide ou expiré")
                            .build()
            );
        }

        // Ici tu pourrais injecter le username dans le contexte de sécurité si besoin
        String username = TokenUtil.getUsername(token);
        requestContext.setProperty("username", username);
    }
}
