package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;

import java.util.List;

public interface IServiceTache {

    public Tache addTacheWithJournal(Tache tache, String journalId);
    public List<Tache> getTachesByStageId(String stageId) ;
    public List<Tache> getTachesByJournalId(String journalId);

    public Tache updateTache(String tacheId, Tache updatedTache);

    public void deleteTacheById(String tacheId);

}
