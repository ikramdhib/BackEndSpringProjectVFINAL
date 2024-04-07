package tn.esprit.pidev.Services;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IServiceStage {
    public Map<String, String> getAllStagesWithUserNames();
    public void sendEmailToEncadrant(String stageId);
    public void sendEmailToStudent(String stageId, String reason) ;
    public void updateEncadrantInfoAndRemoveFromStage(String stageId);
    List<User> getStudentsByEncadrantId(String encadrantId);
    public boolean addAttestationToStage(String stageId, String encadrantId, String etudiantId, MultipartFile image);
    public Stage findStageByUserAndEncadrant(User etudiant, User encadrant) ;
    Stage saveStage(Stage stage);
    Stage findStageById(String stageId);
    public List<String[]> getStudentTimeline(@PathVariable String studentId) ;
    public List<String[]> getStudentTimeline(String studentId, String stageId) ;
    public List<Stage> getStagesByUserId(String userId);
    public List<Stage> getStagesByEncadrantId(String encadrantId) ;

    public InputStream getRapportDeStagePdf(String userId) ;
    public InputStream getAttestationPdfForUser(String userId) ;
    public void uploadPDF(String stageId, MultipartFile pdfFile) throws IOException;
    public InputStream getRapportDeStagePdfAvecOCR(String userId) ;



    }
