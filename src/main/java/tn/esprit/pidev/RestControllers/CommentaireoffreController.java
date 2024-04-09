package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Repositories.CommentoffreRepository;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.CommentaireoffreService;
import tn.esprit.pidev.entities.Commentoffres;
import tn.esprit.pidev.entities.Offre;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/commentaires")
public class CommentaireoffreController {

    @Autowired
    private CommentaireoffreService commentaireoffreService;
    private UserRepository userRepository;
    private OffreRepository offreRepository;
    private CommentoffreRepository commentoffreRepository;

    @GetMapping
    public List<Commentoffres> getAllCommentaires() {
        return commentaireoffreService.getAllCommentaires();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commentoffres> getCommentaireById(@PathVariable("id") String id) {
        Optional<Commentoffres> commentaire = commentaireoffreService.getCommentaireById(id);
        return commentaire.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Commentoffres> addCommentaire(@RequestBody Commentoffres commentaire) {
        Commentoffres addedCommentaire = commentaireoffreService.addCommentaire(commentaire);
        return new ResponseEntity<>(addedCommentaire, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaireById(@PathVariable("id") String id) {
        commentaireoffreService.deleteCommentaireById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/ajouterEtAffecterAUtilisateurEtOffre/{userId}/{offreId}")
    public ResponseEntity<String> ajouterEtAffecterAUtilisateurEtOffre(@PathVariable String userId,
                                                                       @PathVariable String offreId,
                                                                       @RequestBody Commentoffres commentoffres) {
        User user = userRepository.findById(userId).orElse(null);
        Offre offre = offreRepository.findById(offreId).orElse(null);
        log.info("commentaire0");
        if (user != null && offre != null) {
            log.info("commentaire1");
            commentoffres.setUser(user);
            commentoffres.setOffre(offre);
            commentaireoffreService.ajouterEtAffecterAUtilisateurEtOffre(commentoffres);
            return ResponseEntity.ok("Commentaire ajouté et affecté à l'utilisateur et à l'offre avec succès !");
        } else {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec l'ID fourni.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offre non trouvée avec l'ID fourni.");
            }
        }
    }

    @GetMapping("/byOffre/{offreId}")
    public ResponseEntity<List<Commentoffres>> getCommentsByOffre(@PathVariable String offreId) {
        List<Commentoffres> commentaires = commentaireoffreService.getCommentsByOffre(offreId);
        return ResponseEntity.ok(commentaires);
    }

    @PostMapping("/ajouterCommentaire")
    public ResponseEntity<?> ajouterCommentaire(@RequestBody Commentoffres nouveauCommentaire) {
        // Enregistrez le commentaire dans la base de données
        Commentoffres commentaireAjoute = commentoffreRepository.save(nouveauCommentaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentaireAjoute);
    }



}
