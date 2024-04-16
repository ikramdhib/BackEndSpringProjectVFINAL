package tn.esprit.pidev.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.HistoriqueServiceImpl;
import tn.esprit.pidev.entities.Historique;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.Reponse;
import tn.esprit.pidev.entities.User;

import java.util.Date;

@Aspect
@Component
@AllArgsConstructor
public class HistoriqueAspect {
    private HistoriqueServiceImpl historiqueService;
    private UserRepository userRepository;

    @Pointcut("execution(* tn.esprit.pidev.Services.QuestionServiceImpl.addQuestion(..))" +
            "|| execution(* tn.esprit.pidev.Services.ReponseServiceImpl.deleteReponse(..))" +
            "|| execution(* tn.esprit.pidev.Services.ReponseServiceImpl.addReponse(..))")
    public void monitorService() {}

    @AfterReturning("monitorService()")
    public void logServiceAccess(JoinPoint joinPoint, Object object) {
        Historique historique = new Historique();
        historique.setDateAction(new Date());
        historique.setTypeAction("Service Action: " + joinPoint.getSignature().getName());
        String userId = null;

        if (object instanceof Question) {
            userId = String.valueOf(((Question) object).getUser());
        } else if (object instanceof Reponse) {
            userId = String.valueOf(((Reponse) object).getUser());
        }

        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            historique.setUser(user);
            historique.setDetailsAction("Action: " + joinPoint.getSignature().getName() + " for User ID: " + userId);
            historiqueService.saveHistorique(historique, userId);
        }
    }
}