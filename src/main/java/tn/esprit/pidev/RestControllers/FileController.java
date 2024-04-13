package tn.esprit.pidev.RestControllers;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.DemandeRepo;
import tn.esprit.pidev.Services.*;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private OffreServiceImpl offreService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ExtractService extractService;


    public static String UPLOAD_DIRECTORY = "C:\\Users\\LENOVO\\Documents\\GitHub\\ProjectPIAngular\\src\\assets\\cvs\\";

    @Autowired
    private DemandeRepo demandeRepo;

    @Autowired
    private  EmailService sn ;
    private final JavaMailSender javaMailSender;

    public FileController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public static String generateRandomFileName(String originalFileName) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

        // Append current date to ensure uniqueness
        sb.append(currentDate);

        // Append random characters
        for (int i = 0; i < 8; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            sb.append(randomChar);
        }

        // Append original file extension
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            sb.append(originalFileName.substring(lastDotIndex));
        }

        return sb.toString();
    }
    @PostMapping("/add")
    public ResponseEntity<String> addPersonne(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("etat") String etat,
            @RequestParam("studentName") String studentName,
            @RequestParam("studentEmail") String studentEmail,
            @RequestParam("cvPath") MultipartFile cvPath,
            @RequestParam("lettreMotivation") MultipartFile lettreMotivation,
            @RequestParam("idOffre") String idOffre
    ) throws IOException {
        Offre offre = offreService.getOffreByIdv2(idOffre);
        if (offre != null) {
            Demande demande = new Demande();
            demande.setTitre(titre);
            demande.setDescription(description);
            demande.setEtat(etat);
            demande.setStudentEmail(studentEmail);
            demande.setStudentName(studentName);
            demande.setOffre(offre);

            ResponseEntity<String> cvTextResponse = extractService.extractTextFromPDFFile(cvPath);
            if (cvTextResponse.getStatusCode() == HttpStatus.OK) {
                String cvText = cvTextResponse.getBody();

                JSONObject json = new JSONObject(cvText);
                String extractedText = json.getString("text");

                List<String> hashtags = offre.getHashtags();
                List<String> matchingHashtags = findMatchingHashtags(extractedText, hashtags);

                String fileNameCV = generateRandomFileName(cvPath.getOriginalFilename());
                String fileNameLettreMotivation = generateRandomFileName(lettreMotivation.getOriginalFilename());

                demande.setCvPath(fileNameCV);
                demande.setLettreMotivation(fileNameLettreMotivation);

                demandeRepo.save(demande);

                byte[] bytesCV = cvPath.getBytes();
                Path pathCV = Paths.get(UPLOAD_DIRECTORY + fileNameCV);
                Files.write(pathCV, bytesCV);

                byte[] bytesLettreMotivation = lettreMotivation.getBytes();
                Path pathLettreMotivation = Paths.get(UPLOAD_DIRECTORY + fileNameLettreMotivation);
                Files.write(pathLettreMotivation, bytesLettreMotivation);

                if (!matchingHashtags.isEmpty()) {
                    sn.sendEmail(studentEmail, demande, "accepted");
                    return new ResponseEntity<>("accepted", HttpStatus.OK);
                } else {
                    sn.sendEmail(studentEmail, demande, "denied");
                    return new ResponseEntity<>("denied", HttpStatus.OK);
                }
            } else {
                System.out.println("Error extracting text from CV PDF");
                return new ResponseEntity<>("Error extracting text from CV PDF", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Offre non trouvée", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/GetOffres")
    public ResponseEntity<List<Offre>> getAllOffres() {
        List<Offre> offres = offreService.getAllOffre(); // Supposons que vous avez un service pour gérer les offres
        return ResponseEntity.ok().body(offres);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Offre>> getOffresByType(@PathVariable Type type) {
        List<Offre> offres = offreService.findByType(type);
        return ResponseEntity.ok(offres);
    }



    private List<String> findMatchingHashtags(String cvText, List<String> hashtags) {
        String[] cvWords = cvText.toLowerCase().split("\\W+");

        return hashtags.stream()
                .filter(hashtag -> containsAnyWord(hashtag.toLowerCase(), cvWords))
                .collect(Collectors.toList());
    }

    private boolean containsAnyWord(String text, String[] words) {
        for (String word : words) {
            System.out.println(word);
            if (text.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file) throws IOException {
        //byte[] bytes = file.getBytes();
        // Path path = Paths.get(UPLOAD_DIRECTORY + file);
        // Files.write(path, bytes);
        return new ResponseEntity<>(fileService.addFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        LoadFile loadFile = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }
}