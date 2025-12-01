package org.ek.portfoliobackend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data // indeholder getter/setter + toString(), equals(), hashCode() (Sidste to er måske ikke nødvendige, men de er der)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotNull(message = "Projektet skal have en title.")
    @NotBlank
    private String title;

    @NotNull(message = "Der skal være en beskrivelse til projektet.")
    private String description;

    // ENUMS
    private ServiceCategory serviceCategory; // Kan vi ændre klassen til at hedde JobTypeCategory i stedet?
    private Customertype costumerType;


    private LocalDate executionDate;



}
