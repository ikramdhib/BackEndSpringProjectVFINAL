package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Stage;

public interface IServiceStage {
    void ajouterEtAffecterStageAUtilisateur(Stage stage, String userId);
}
