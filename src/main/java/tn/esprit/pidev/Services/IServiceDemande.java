package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Demande;

import java.util.List;
import java.util.Optional;

public interface IServiceDemande {
    List<Demande> getAllDemandes();
    Optional<Demande> getDemandeById(String id);
    Demande adddemande(Demande demande,String userId);
    Demande updateDemande(Demande demande);
    boolean deleteDemande(String id);

    // Demande assignerDemandeAOffre(String idDemande, String idOffre);
}