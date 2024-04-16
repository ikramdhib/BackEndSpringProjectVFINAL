package tn.esprit.pidev.RestControllers.Reclamtions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.Services.ReclamationService.ReclamationServiceImpl;
import tn.esprit.pidev.entities.Reclamation;

@RestController
@AllArgsConstructor
public class ReclamationRestController {

    public ReclamationServiceImpl reclamationService ;


    @PostMapping("/addReclamation")
    public ResponseEntity<?> addReclamation(Reclamation reclamation){
        Reclamation reclamation1 = reclamationService.addReclamation(reclamation);
        if(reclamation1!=null){
            return ResponseEntity.status(HttpStatus.OK).body(reclamation1);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("try again");
    }



}
