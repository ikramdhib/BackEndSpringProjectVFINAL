package tn.esprit.pidev.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import tn.esprit.pidev.Services.HistoriqueServiceImpl;
import tn.esprit.pidev.entities.Historique;
import tn.esprit.pidev.entities.Question;

import java.util.Date;

@Aspect
@Component
@AllArgsConstructor
public class HistoriqueAspect {
    private HistoriqueServiceImpl historiqueService;
    @Pointcut("execution(* tn.esprit.pidev.Services.QuestionServiceImpl.addQuestion(..))" +
            "|| execution(* tn.esprit.pidev.Services.ReponseServiceImpl.deleteReponse(..))" +
            "|| execution(* tn.esprit.pidev.Services.QuestionServiceImpl.getQuestionById(..))" +
            "|| execution(* tn.esprit.pidev.Services.ReponseServiceImpl.addReponse(..))")
    public void monitorService() {}

    @AfterReturning("monitorService()")
    public void logServiceAccess(JoinPoint joinPoint) {
        Historique historique = new Historique();
        // Configurez ici les propriétés de votre entité Historique
        historique.setDateAction(new Date());
        historique.setTypeAction("Question Service Action");
        historique.setDetailsAction("Méthode appelée: " + joinPoint.getSignature().getName());

        // Exemple d'extraction d'argument - adapté selon vos besoins
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof Question) {
            Question question = (Question) args[0];
            historique.setDetailsAction(historique.getDetailsAction() + " avec question ID: " + question.getId());
        }
        historiqueService.saveHistorique(historique);
    }
}
