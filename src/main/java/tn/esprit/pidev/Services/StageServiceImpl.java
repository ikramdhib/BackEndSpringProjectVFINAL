package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StageServiceImpl implements IServiceStage{
    private final StageRepository stageRepository;
    private final UserRepository userRepository;

    @Autowired
    public StageServiceImpl(StageRepository stageRepository, UserRepository userRepository) {
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void ajouterEtAffecterStageAUtilisateur(Stage stage, String userId) {

        User user = userRepository.findById(userId).orElse(null);
        stage.setUser(user);
        stageRepository.save(stage);
        log.info(stage.type.name()+"&&&&&&&&&&&&&&&&&&&");

    }

    @Override
    public void saveDemandeStage(String userId, String demandeStageContent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));

        Stage stage = new Stage();
        stage.setUser(user);
        stage.setDemandeS(demandeStageContent);

        stageRepository.save(stage);
    }

    @Override
    public List<Stage> getStagesByUserId(String userId) {
        return stageRepository.findByUserId(userId);
    }

    @Override
    public Stage updateStage(String stageId, Stage updatedStage) {

            Stage existingStage = stageRepository.findById(stageId).orElse(null);

            if (existingStage != null) {
                // Mettez à jour les champs nécessaires du stage existant avec les nouvelles valeurs
                existingStage.setNomSociete(updatedStage.getNomSociete());
                existingStage.setNumSociete(updatedStage.getNumSociete());
                existingStage.setEmailSociete(updatedStage.getEmailSociete());
                existingStage.setNomCoach(updatedStage.getNomCoach());
                existingStage.setPrenomCoach(updatedStage.getPrenomCoach());
                existingStage.setNumCoach(updatedStage.getNumCoach());
                existingStage.setEmailCoach(updatedStage.getEmailCoach());
                existingStage.setStartAt(updatedStage.getStartAt());
                existingStage.setEndAt(updatedStage.getEndAt());
                existingStage.setType(updatedStage.getType());
                // Enregistrez les modifications dans la base de données
                return stageRepository.save(existingStage);
            }

            return null; // Ou lancez une exception selon vos besoins
        }

    @Override
    public void deleteStageById(String stageId) {
        stageRepository.deleteById(stageId);
    }

    @Override
    public Stage getStageById(String stageId) {
        return stageRepository.findById(stageId).orElse(null);
    }

    @Override
    public boolean isJournalAssociated(String stageId) {
        Stage stage = stageRepository.findById(stageId).orElse(null);
        boolean isAssociated = (stage != null && stage.getJournal() != null);

        // Ajouter des logs de débogage
        System.out.println("Stage ID: " + stageId);
        System.out.println("Is Journal Associated: " + isAssociated);

        return isAssociated;
    }

}
