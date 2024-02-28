package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Tache;

public interface IServiceTache {

    public Tache addTacheWithJournal(Tache tache, String journalId);

}
