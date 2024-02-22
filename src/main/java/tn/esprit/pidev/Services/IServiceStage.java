package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Stage;

import java.util.Optional;

public interface IServiceStage {
    void ajouterEtAffecterStageAUtilisateur(Stage stage, String userId);
    public void saveDemandeStage(String userId, String demandeStageContent);

    public Stage getStageByUserId(String userId);

}
