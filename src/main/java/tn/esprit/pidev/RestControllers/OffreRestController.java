package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.OffreServiceImpl;
import tn.esprit.pidev.entities.Offre;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/offres")
public class OffreRestController {

    @Autowired
    private OffreServiceImpl offreService;
    @GetMapping
    public List<Offre> getAllOffres() {
        return offreService.getAllOffres();
    }
    private String generateLogoUrlForEntreprise(String nomEntreprise) {
        // Ici, vous générez l'URL du logo en fonction du nom de l'entreprise
        // Vous pouvez récupérer l'URL à partir d'une source de données ou la construire dynamiquement
        return "http://example.com/logos/" + nomEntreprise + ".png";
    }


    @GetMapping("/byEntreprise")
    public Map<String, List<Offre>> groupOffresByEntreprise() {
        Map<String, List<Offre>> offresByEntreprise = offreService.groupOffresByEntreprise();

        // Transformez les données binaires du logo en URL du logo pour chaque entreprise
        offresByEntreprise.forEach((entreprise, offres) -> {
            String logoUrl = generateLogoUrlForEntreprise(entreprise);
            offres.forEach(offre -> {
                offre.setLogoentrepriseUrl(logoUrl);
            });
        });

        return offresByEntreprise;
    }




    @GetMapping("/{_id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable String _id) {
        Optional<Offre> offre = offreService.getOffreById(_id);
        return offre.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/add")
    public ResponseEntity<Offre> createOffre(@RequestParam("file") MultipartFile file,
                                             @RequestParam("nomEntreprise") String nomEntreprise,
                                             @RequestParam("nomEncadrant") String nomEncadrant,
                                             @RequestParam("prenomEncadrant") String prenomEncadrant,
                                             @RequestParam("email") String email,
                                             @RequestParam("description") String description,
                                             @RequestParam("userId") String userId) { // Ajoutez un paramètre pour l'ID de l'utilisateur
        try {
            byte[] fileBytes = file.getBytes();

            // Créez une nouvelle offre
            Offre nouvelleOffre = new Offre();
            nouvelleOffre.setNomEntreprise(nomEntreprise);
            nouvelleOffre.setLogoentreprise(fileBytes);
            nouvelleOffre.setNomEncadrant(nomEncadrant);
            nouvelleOffre.setPrenomEncadrant(prenomEncadrant);
            nouvelleOffre.setEmail(email);
            nouvelleOffre.setDescription(description);

            // Utilisez le service pour créer l'offre en associant l'ID de l'utilisateur
            Offre savedOffre = offreService.createOffre(nouvelleOffre, userId);
            return new ResponseEntity<>(savedOffre, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






    @PutMapping("/update/{_id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable String _id, @RequestBody Offre updatedOffre) {
        Offre updated = offreService.updateOffre(_id, updatedOffre);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{_id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable String _id) {
        offreService.deleteOffre(_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
