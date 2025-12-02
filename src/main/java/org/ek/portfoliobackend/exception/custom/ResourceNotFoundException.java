package org.ek.portfoliobackend.exception.custom;

/**
 * Kastes når en ressource ikke findes i databasen (resultere i HTTP 404).
 */

public class ResourceNotFoundException extends RuntimeException {

    // Konstruktør 1: med en super message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Konstruktør 2: Resource navn + ID
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s med id %d blev ikke fundet", resourceName, id));
        // Format: "Project med id 42 blev ikke fundet"
    }

    // Konstruktør 3: Resource navn + string identifier
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s '%s' blev ikke fundet", resourceName, identifier));
        // Format: "Project 'AlgeNord' blev ikke fundet"
    }
}
