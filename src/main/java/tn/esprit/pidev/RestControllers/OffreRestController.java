package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.OffreServiceImpl;
import tn.esprit.pidev.entities.Offre;
import tn.esprit.pidev.entities.Type;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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



    @GetMapping("/byEntreprise")
    public Map<String, List<Offre>> groupOffresByEntreprise() {
        Map<String, List<Offre>> offresByEntreprise = offreService.groupOffresByEntreprise();

        return offresByEntreprise;
    }




    @GetMapping("/{_id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable String _id) {
        Optional<Offre> offre = offreService.getOffreById(_id);
        return offre.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addOffreWithImage(@RequestParam("nomEntreprise") String nomEntreprise,
                                               @RequestParam("nomEncadrant") String nomEncadrant,
                                               @RequestParam("prenomEncadrant") String prenomEncadrant,
                                               @RequestParam("email") String email,
                                               @RequestParam("description") String description,
                                               @RequestParam("datedebut_stage") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date datedebut_stage,
                                               @RequestParam("datefin_stage") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date datefin_stage,
                                               @RequestParam("type") Type type,
                                               @RequestParam("duree") Number duree,
                                               @RequestParam(value = "image") MultipartFile imageFile,
                                               @RequestParam("userId") String userId) {
        try {
            Offre offre = new Offre();
            offre.setNomEntreprise(nomEntreprise);
            offre.setNomEncadrant(nomEncadrant);
            offre.setPrenomEncadrant(prenomEncadrant);
            offre.setEmail(email);
            offre.setDescription(description);
            offre.setDatedebut_stage(datedebut_stage);
            offre.setDatefin_stage(datefin_stage);
            offre.setType(type);
            offre.setDuree(duree);

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = offreService.saveImage(imageFile);
                offre.setImageUrl(imageUrl);
            }

            Offre savedOffre = offreService.createOffre(offre, userId);
            return ResponseEntity.ok(savedOffre);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de l'enregistrement de l'offre ou de l'image.");
        }
    }

    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le fichier ne doit pas être vide");
        }

        try {
            String imageUrl = offreService.saveImage(imageFile); // Save the image and get its URL
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl); // Send the image URL in the response
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            String errorMessage = "Erreur lors du téléchargement de l'image: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMessage);
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
