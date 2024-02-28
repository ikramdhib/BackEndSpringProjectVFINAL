package tn.esprit.pidev.RestControllers;
import com.itextpdf.layout.Document;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.Services.IServiceUser;
import tn.esprit.pidev.entities.Role;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StageRestController {
        @Autowired
        private IServiceStage iServiceStage;
        private IServiceUser iServiceUser;
        @GetMapping("/userNames")
        public Map<String, String> getAllStagesWithUserNames() {
                return iServiceStage.getAllStagesWithUserNames();
        }
        @PostMapping("/sendEmailToEncadrant/{stageId}")
        public String sendEmailToEncadrant(@PathVariable String stageId) {
                try {
                        iServiceStage.sendEmailToEncadrant(stageId);
                        iServiceStage.updateEncadrantInfoAndRemoveFromStage(stageId); // Appel de la méthode pour mettre à jour l'utilisateur et supprimer les informations de l'encadrant de la table Stage
                        return "E-mail envoyé à l'encadrant avec succès.";
                } catch (Exception e) {
                        e.printStackTrace();
                        return "Une erreur s'est produite lors de l'envoi de l'e-mail à l'encadrant.";
                }
        }
        @PostMapping("/sendEmailToStudent/{stageId}/{reason}")
        public String sendEmailToStudent(@PathVariable String stageId,@PathVariable String reason) {
                try {
                        iServiceStage.sendEmailToStudent(stageId,reason);
                        return "E-mail envoyé à l'étudiant avec succès.";
                } catch (Exception e) {
                        e.printStackTrace();
                        return "Une erreur s'est produite lors de l'envoi de l'e-mail à l'étudiant.";
                }

}
        @GetMapping("/studentsByEncadrant/{encadrantId}")
        public List<User> getStudentsByEncadrantId(@PathVariable String encadrantId) {
                return iServiceStage.getStudentsByEncadrantId(encadrantId);
        }
        /////////attestation

        @PostMapping("/{stageId}/addAttestation/{encadrantId}/{etudiantId}")
        public ResponseEntity<?> addAttestationToStage(@PathVariable String stageId,
                                                       @PathVariable String encadrantId,
                                                       @PathVariable String etudiantId,
                                                       @RequestParam("pdfFile") MultipartFile pdfFile,
                                                       @RequestParam("htmlContent") String htmlContent) {
                try {
                        byte[] pdfBytes = generatePdfFromHtml(htmlContent);

                        // Récupérer l'étudiant et l'encadrant
                        User etudiant = iServiceUser.findUserById(etudiantId);
                        User encadrant = iServiceUser.findUserById(encadrantId);

                        // Vérifier si l'étudiant et l'encadrant existent
                        if (etudiant != null && encadrant != null) {
                                // Récupérer le stage associé à l'étudiant et à l'encadrant
                                Stage stage = iServiceStage.findStageByUserAndEncadrant(etudiant, encadrant);

                                // Vérifier si le stage existe
                                if (stage != null) {
                                        stage.setAttestationPdf(pdfBytes);
                                        iServiceStage.saveStage(stage);
                                        return ResponseEntity.ok("Attestation ajoutée avec succès au stage.");
                                } else {
                                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le stage n'a pas été trouvé.");
                                }
                        } else {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'étudiant ou l'encadrant n'a pas été trouvé.");
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'ajout de l'attestation au stage.");
                }
        }

        // Méthode pour convertir du HTML en PDF
        private byte[] generatePdfFromHtml(String htmlContent) throws IOException {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);
                document.add(new Paragraph(htmlContent));
                document.close();
                return outputStream.toByteArray();
        }
}


