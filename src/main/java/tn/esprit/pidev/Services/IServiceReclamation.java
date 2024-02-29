package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Reclamation;

import java.util.List;

public interface IServiceReclamation {

    Reclamation addReclamation(Reclamation reclamation);

    Reclamation updateReclamation(Reclamation reclamation);

    List<Reclamation> getAllReclamation();

    Reclamation getDetailsOfReclamation(String id);
}
