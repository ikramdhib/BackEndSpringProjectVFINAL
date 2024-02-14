package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "offres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offre {
    @Id
    private String id;

    @NotBlank (message = "Le titre ne peut pas être vide")
    private String titre;

    @NotBlank(message = "La description ne peut pas être vide")
    private String description;

    @NotBlank(message = "La localisation ne peut pas être vide")
    private String localisation;

    @NotNull (message = "La date de début ne peut pas être nulle")
    private Date dateDebut;

    @NotNull(message = "La date de fin ne peut pas être nulle")
    private Date dateFin;

    private List<String> competencesRequises;

    private String entreprise;
}
