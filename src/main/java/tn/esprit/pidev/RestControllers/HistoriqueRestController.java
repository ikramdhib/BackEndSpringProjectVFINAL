package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.HistoriqueServiceImpl;
import tn.esprit.pidev.entities.Historique;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class HistoriqueRestController {
    private HistoriqueServiceImpl historiqueService;
    @GetMapping("/getHistoriqueByUser/{userId}")
    public List<Historique> getHistoriqueByUser(@PathVariable String userId){
        List<Historique> historiques = historiqueService.findBuUserId(userId);
        return historiques;
    }
    @DeleteMapping("/deleteHistorique/{historiqueId}")
    public void deleteHistorique(@PathVariable String historiqueId) {
        historiqueService.delete(historiqueId);
    }
}
