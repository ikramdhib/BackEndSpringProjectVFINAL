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
    public Stage getStageByUserId(String userId) {
        return stageRepository.findByUser_id(userId); // Assurez-vous d'adapter cela à votre modèle de données
    }


}
