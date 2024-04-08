package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Journal;
import java.util.List;
import java.util.Map;

public interface IServiceJournal {
    public List<Map<String, Object>> getJournalsWithTasksByStudentId(String studentId) ;
    public Journal addJournal(Journal journal, String stageId);
    public Journal getJournal(String journalId);
    }
