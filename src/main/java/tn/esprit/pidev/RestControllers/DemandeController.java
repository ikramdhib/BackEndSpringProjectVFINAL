package tn.esprit.pidev.RestControllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.IServiceDemande;
import tn.esprit.pidev.entities.Demande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")


public class DemandeController {
    @Autowired
    private IServiceDemande demandeService;

    @Value("/Demande/${file.upload-dir}")
    private String uploadDir;

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
    @PostMapping("/CreateDemande")
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande) {
        Demande createdDemande = demandeService.createDemande(demande);
        return new ResponseEntity<>(createdDemande, HttpStatus.CREATED);
    }

    // Endpoint to update a demand
    @PutMapping("/UpdateDemande/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable String id, @RequestBody Demande updatedDemande) {
        Optional<Demande> existingDemande = demandeService.getDemandeById(id);
        if (existingDemande.isPresent()) {
            updatedDemande.setId(id);
            Demande savedDemande = demandeService.updateDemande(updatedDemande);
            return new ResponseEntity<>(savedDemande, HttpStatus.OK);
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
    @PutMapping("/{demandeId}/assigner-offre/{offreId}")
    public ResponseEntity<Demande> assignerDemandeAOffre(@PathVariable String demandeId, @PathVariable String offreId) {
        Demande demande = demandeService.assignerDemandeAOffre(demandeId, offreId);
        return new ResponseEntity<>(demande, HttpStatus.OK);
    }
    @PostMapping("/CreateDemandeWithFile")
    public ResponseEntity<Demande> createDemandeWithFile(@RequestBody Demande demande,
                                                 @RequestParam("cv") MultipartFile cvFile) {
        // Logique pour sauvegarder le fichier CV
        String cvPath = saveCVFile(cvFile);

        // Associer le chemin du fichier CV à la demande
        demande.setCvPath(cvPath);

        // Créer la demande
        Demande createdDemande = demandeService.createDemande(demande);

        return new ResponseEntity<>(createdDemande, HttpStatus.CREATED);
    }

    // Méthode pour sauvegarder le fichier CV
    private String saveCVFile(MultipartFile cvFile) {
        String fileName = StringUtils.cleanPath(cvFile.getOriginalFilename());
        String filePath = Paths.get(uploadDir, fileName).toString();

        try {
            Files.copy(cvFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Impossible de sauvegarder le fichier CV. Veuillez réessayer!", e);
        }
    }

}
