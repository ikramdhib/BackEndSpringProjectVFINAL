package tn.esprit.pidev.RestControllers;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                                                       @RequestParam("htmlContent") String htmlContent,
                                                       @RequestParam(value = "logoFile", required = false) MultipartFile logoFile) {
                try {
                        byte[] pdfBytes = generatePdfFromHtml(htmlContent, logoFile);

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

        // Méthode pour convertir du HTML en PDF avec le logo de la société
        private byte[] generatePdfFromHtml(String htmlContent, MultipartFile logoFile) throws IOException {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Ajouter le logo de la société s'il est fourni
                if (logoFile != null && !logoFile.isEmpty()) {
                        ImageData logoData = ImageDataFactory.create(logoFile.getBytes());
                        Image logo = new Image(logoData);
                        document.add(logo);
                }

                document.add(new Paragraph(htmlContent));
                document.close();
                return outputStream.toByteArray();
        }
        @GetMapping("/{stageId}/downloadAttestation/{encadrantId}/{studentId}")
        public ResponseEntity<ByteArrayResource> downloadAttestation(@PathVariable String stageId,
                                                                     @PathVariable String encadrantId,
                                                                     @PathVariable String studentId) {
                // Récupérer le stage associé à l'étudiant et à l'encadrant
                Stage stage = iServiceStage.findStageById(stageId);

                // Vérifier si le stage existe
                if (stage != null) {
                        // Récupérer le PDF de l'attestation à partir du stage
                        byte[] pdfBytes = stage.getAttestationPdf();

                        // Créer une ressource ByteArrayResource à partir des bytes PDF
                        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

                        // Retourner une réponse avec la ressource PDF
                        return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_PDF)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attestation.pdf\"")
                                .body(resource);
                } else {
                        // Si le stage n'est pas trouvé, retourner une réponse 404 Not Found
                        return ResponseEntity.notFound().build();
                }

        }
        @GetMapping("/students/{studentId}/timeline")
        public List<String[]> getStudentTimeline(@PathVariable String studentId) {
                return iServiceStage.getStudentTimeline(studentId);
        }
        ///
        @GetMapping("/students/{studentId}/stages/{stageId}")
        public ResponseEntity<List<String[]>> getStudentTimeline(@PathVariable String studentId, @PathVariable String stageId) {
                List<String[]> timeline = iServiceStage.getStudentTimeline(studentId, stageId);

                if (timeline == null) {
                        return ResponseEntity.notFound().build(); // Gérer le cas où le stage n'existe pas ou ne correspond pas à l'étudiant
                }

                return ResponseEntity.ok(timeline);
        }
        @GetMapping("/user/{userId}")
        public List<Stage> getStagesByUserId(@PathVariable String userId) {
                return iServiceStage.getStagesByUserId(userId);
        }


        @GetMapping("/encadrant/{encadrantId}")
        public List<Stage> getStagesByEncadrantId(@PathVariable String encadrantId) {
                return iServiceStage.getStagesByEncadrantId(encadrantId);
        }

}



