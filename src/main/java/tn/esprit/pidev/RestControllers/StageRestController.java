package tn.esprit.pidev.RestControllers;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.Services.UserServices.IServiceUser;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StageRestController {
        @Autowired
        private IServiceStage iServiceStage;
        private IServiceUser iServiceUser;

    private final IServiceStage stageService;

    @Autowired
    public StageRestController(IServiceStage stageService) {
        this.stageService = stageService;
    }
    @Autowired
    UserRepository userRepository ;
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
                iServiceStage.sendEmailToStudent(stageId, reason);
                return "E-mail envoyé à l'étudiant avec succès.";
            } catch (Exception e) {
                e.printStackTrace();
                return "Une erreur s'est produite lors de l'envoi de l'e-mail à l'étudiant.";
            }
        }


    @PostMapping("/ajouterEtAffecterStageAUtilisateur/{id}")
    public ResponseEntity<String> ajouterEtAffecterStageAUtilisateur(@PathVariable String id , @RequestBody Stage stage) {
        System.out.println("testttt");
        User user = userRepository.findById(id).get() ;
        stage.setUser(user);
        System.out.println("hello stage user "+stage.getUser().getFirstName());
        if (stage.getUser() != null) {
            String userId = stage.getUser().getId();
            stageService.ajouterEtAffecterStageAUtilisateur(stage, userId);
            return ResponseEntity.ok("Stage ajouté et affecté à l'utilisateur avec succès !");
        } else {
            // Gérer le cas où User est null, par exemple, en renvoyant une erreur appropriée.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur associé au stage est null.");
        }
    }

    @PostMapping("/save-demande-stage/{userId}")
    public ResponseEntity<String> saveDemandeStage(@PathVariable String userId, @RequestBody String demandeStageContent) {
        stageService.saveDemandeStage(userId, demandeStageContent);
        return ResponseEntity.ok("Demande de stage enregistrée avec succès.");
    }


    @PutMapping("/updateStage/{stageId}")
    public ResponseEntity<Stage> updateStage(@PathVariable String stageId, @RequestBody Stage updatedStage) {
        Stage updated = stageService.updateStage(stageId, updatedStage);

        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{stageId}")
    public ResponseEntity<?> deleteStage(@PathVariable String stageId) {
        try {
            // Appeler le service pour supprimer le stage
            stageService.deleteStageById(stageId);
            return new ResponseEntity<>("Stage supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la suppression du stage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getStage/{stageId}")
    public Stage getStageById(@PathVariable String stageId){
       return stageService.getStageById(stageId);
    }

    @GetMapping("/isJournalAssociated/{stageId}")
    public ResponseEntity<Boolean> isJournalAssociated(@PathVariable String stageId) {
        boolean isAssociated = stageService.isJournalAssociated(stageId);
        return ResponseEntity.ok(isAssociated);
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


        @GetMapping("/rapportDeStagePdfAvecTIKA/{userId}")
        public ResponseEntity<?> getRapportDeStagePdfAvecOCR(@PathVariable String userId) {
                InputStream inputStream = iServiceStage.getRapportDeStagePdfAvecOCR(userId);
                if (inputStream != null) {
                        // Renvoie le PDF s'il a pu être extrait et respecte les normes
                        return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(new InputStreamResource(inputStream));
                } else {
                        // Le texte extrait ne respecte pas les normes
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le texte extrait ne respecte pas les normes");
                }
        }

        @PostMapping("/stages/{stageId}/upload-pdf")
        public ResponseEntity<String> uploadPDF(@PathVariable String stageId, @RequestParam("pdf") MultipartFile pdfFile) {
                try {
                        iServiceStage.uploadPDF(stageId, pdfFile);
                        return ResponseEntity.status(HttpStatus.OK).body("Fichier PDF d'attestation enregistré avec succès.");
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement du fichier PDF d'attestation.");
                }
        }

        @GetMapping("/attestation/{userId}")
        public ResponseEntity<InputStreamResource> downloadAttestationPdf(@PathVariable String userId) {
                InputStream attestationStream = iServiceStage.getAttestationPdfForUser(userId);

                if (attestationStream != null) {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_PDF);
                        headers.setContentDispositionFormData("attachment", "attestation.pdf");

                        return ResponseEntity
                                .ok()
                                .headers(headers)
                                .body(new InputStreamResource(attestationStream));
                } else {
                        return ResponseEntity.notFound().build();
                }
        }

        @GetMapping("/rapports/{userId}")
        public ResponseEntity<InputStreamResource> getRapportDeStage(@PathVariable String userId) {
                InputStream pdfStream = iServiceStage.getRapportDeStagePdf(userId);

                if (pdfStream != null) {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_PDF);
                        headers.setContentDispositionFormData("attachment", "rapport_de_stage.pdf");

                        return ResponseEntity
                                .ok()
                                .headers(headers)
                                .body(new InputStreamResource(pdfStream));
                } else {
                        return ResponseEntity.notFound().build();
                }
        }

    @PostMapping("/{stageId}/rapportPdf")
    public ResponseEntity<String> uploadRapportPdf(@PathVariable String stageId, @RequestParam("file") MultipartFile file) {
        try {
            // Récupérer le stage existant à partir de son ID
            Stage stage = stageService.getStageById(stageId);
            if (stage != null) {
                // Convertir le fichier MultipartFile en un tableau de bytes
                byte[] pdfBytes = file.getBytes();
                // Mettre à jour l'attribut rapportPdf du stage avec le nouveau PDF
                stage.setRapportPdf(pdfBytes);
                // Enregistrer le stage mis à jour dans la base de données
                stageService.updateStage2(stageId,stage);
                return ResponseEntity.ok("Rapport PDF ajouté avec succès.");
            } else {
                return ResponseEntity.notFound().build(); // Gérer le cas où le stage n'est pas trouvé
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du rapport PDF.");
        }
    }




    @GetMapping("/{stageId}/rapportExiste")
    public boolean rapportExiste(@PathVariable String stageId) {
        return stageService.rapportExistePourStage(stageId);
    }

    @GetMapping("/attestationstage/{stageId}")
    public ResponseEntity<ByteArrayResource> getAttestation(@PathVariable String stageId) {
        Stage stage = stageService.findById(stageId);
        ByteArrayResource resource = new ByteArrayResource(stage.getAttestationPdf());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attestation.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(stage.getAttestationPdf().length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/stage/getAllWithUserId/{id}")
    public ResponseEntity<?> getAllStageWithUserID(@PathVariable String id){
            List<Stage> stages = stageService.getAllStageWithUsrId(id);
            return ResponseEntity.status(HttpStatus.OK).body(stages);
    }

}



