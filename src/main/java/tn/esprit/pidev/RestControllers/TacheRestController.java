package tn.esprit.pidev.RestControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceTache;
import tn.esprit.pidev.entities.Tache;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600 , allowCredentials = "true")

@RequestMapping("/api/taches")
public class TacheRestController {

    public IServiceTache serviceTache;

    public TacheRestController(IServiceTache serviceTache) {

        this.serviceTache = serviceTache;
    }

    @PostMapping("/addWithJournal/{journalId}")
    public Tache addTacheWithJournal(@RequestBody Tache tache, @PathVariable String journalId) {
        return serviceTache.addTacheWithJournal(tache, journalId);
    }

}
