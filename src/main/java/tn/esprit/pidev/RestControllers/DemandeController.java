package tn.esprit.pidev.RestControllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.EmailService;
import tn.esprit.pidev.Services.IServiceDemande;
import tn.esprit.pidev.entities.Demande;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    @Autowired
    private IServiceDemande demandeService;

    private final JavaMailSender javaMailSender;


    @Value("/Demande/${file.upload-dir}")
    private String uploadDir;

    public DemandeController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }



    // Endpoint to get all demands
    @GetMapping("/GetListDemande")
    public ResponseEntity<List<Demande>> getAllDemandes() {
        List<Demande> demandes = demandeService.getAllDemandes();
        return new ResponseEntity<>(demandes, HttpStatus.OK);
    }

    // Endpoint to get a demand by ID
    @GetMapping("/GetDemande/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable String id) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        return demande.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to create a new demand
    //@PostMapping("/CreateDemande")
    //public ResponseEntity<Demande> createDemande(@RequestBody Demande demande) {
    // Demande createdDemande = demandeService.createDemande(demande);
    //  return new ResponseEntity<>(createdDemande, HttpStatus.CREATED);
    //}


    // Endpoint to update a demand
    // Endpoint to update a demand
    // Endpoint to update a demand
    @PutMapping("/UpdateDemande/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable String id, @RequestBody Demande updatedDemande) {
        Optional<Demande> existingDemande = demandeService.getDemandeById(id);
        if (existingDemande.isPresent()) {
            Demande existingDemandeInstance = existingDemande.get();

            // Check if two hours have passed since creation
            long hoursElapsed = Duration.between(existingDemandeInstance.getCreatedAt(), LocalDateTime.now())
                    .toHours();
            if (hoursElapsed >= 2) {
                // Update the fields you want to change
                existingDemandeInstance.setTitre(updatedDemande.getTitre());
                existingDemandeInstance.setStudentName(updatedDemande.getStudentName());
                existingDemandeInstance.setStudentEmail(updatedDemande.getStudentEmail());

                // Call the service to save the updated demand
                Demande savedDemande = demandeService.updateDemande(existingDemandeInstance);

                return new ResponseEntity<>(savedDemande, HttpStatus.OK);
            } else {
                // Return a response indicating that the update is not allowed
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }





    // Endpoint to delete a demand
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable String id) {
        if (demandeService.deleteDemande(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/demandes/getDemandesOffre/{id}")
    public ResponseEntity<?> getDemandesWithOffresId(@PathVariable  String id){
        List<Demande> demandes = demandeService.getDemandeWithOffreId(id);
        return ResponseEntity.status(HttpStatus.OK).body(demandes);
    }

}