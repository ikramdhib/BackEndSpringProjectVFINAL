package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Journal;

public interface IServiceJournal {
    public Journal addJournal(Journal journal, String stageId);

}
