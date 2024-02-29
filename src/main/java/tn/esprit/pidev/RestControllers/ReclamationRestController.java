package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.Services.IServiceReclamation;

@RestController
@AllArgsConstructor
public class ReclamationRestController {

    private final IServiceReclamation serviceReclamation ;




}
