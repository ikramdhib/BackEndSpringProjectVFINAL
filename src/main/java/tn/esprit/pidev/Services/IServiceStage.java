package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Stage;

import java.util.List;
import java.util.Optional;

public interface IServiceStage {
    void ajouterEtAffecterStageAUtilisateur(Stage stage, String userId);
    public void saveDemandeStage(String userId, String demandeStageContent);

    public List<Stage> getStagesByUserId(String userId);

    public Stage updateStage(String stageId, Stage updatedStage);

    public void deleteStageById(String stageId);

    public Stage getStageById(String stageId);

    public boolean isJournalAssociated(String stageId);
}
