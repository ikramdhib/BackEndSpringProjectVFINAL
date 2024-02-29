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
import tn.esprit.pidev.entities.Reponse;

import java.util.Date;

@Aspect
@Component
@AllArgsConstructor
public class HistoriqueAspect {
    private HistoriqueServiceImpl historiqueService;
    @Pointcut("execution(* tn.esprit.pidev.Services.QuestionServiceImpl.addQuestion(..))" +
            "|| execution(* tn.esprit.pidev.Services.ReponseServiceImpl.deleteReponse(..))" +
            "|| execution(* tn.esprit.pidev.Services.ReponseServiceImpl.addReponse(..))")
    public void monitorService() {} //elle est juste un marqueur utilisé par Spring AOP pour savoir où appliquer les conseils définis dans vos aspects.

    @AfterReturning("monitorService()")
    public void logServiceAccess(JoinPoint joinPoint) {
        Historique historique = new Historique();

        historique.setDateAction(new Date());
        historique.setTypeAction("Question Service Action");
        historique.setDetailsAction("Méthode appelée: " + joinPoint.getSignature().getName());

        // déclaration d'un tableau d'objets 'args' / attribuer les arguments de la méthode intercepté par le pointcut
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof Question) { //s'assurer que le type de l'objet correspond à ce que le code s'attend à manipuler.
            Question question = (Question) args[0];  //pour traiter l'objet en tant qu'instance spécifique de Question et accéder à ses méthodes et propriétés.
            historique.setDetailsAction(historique.getDetailsAction() + " avec question Name: " + question.getTitre());
        }
        if (args != null && args.length > 0 && args[0] instanceof Reponse) {
            Reponse reponse = (Reponse) args[0];
            historique.setDetailsAction(historique.getDetailsAction() + " avec reponse ID: " + reponse.getId());
        }
        historiqueService.saveHistorique(historique);
    }
}
