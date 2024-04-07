package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Tache;

import java.util.List;

public interface ITacheService {
    public List<Tache> getTasksForUserAndStage(String userId, String stageId) ;
    public List<Tache> getTasksForStudent(String studentId) ;
    public List<Tache> getTasksForUser(String userId) ;

    public List<Tache> getTachesByJournalId(String journalId);
    public void rejectTask(String taskId, String rejectionReason);
    public void validateTask(String tacheId) ;

    }
