package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.PdfService;

@RestController
public class DownloadController {

    private final PdfService pdfService;



    public DownloadController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/api/download/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody String fullHtmlContent) throws Exception {
        String bodyContent = extractBodyContent(fullHtmlContent);
        byte[] pdfBytes = pdfService.generatePdfFromHtml(bodyContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "demande_stage.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private String extractBodyContent(String fullHtmlContent) {
        int start = fullHtmlContent.indexOf("<body>");
        int end = fullHtmlContent.lastIndexOf("</body>");
        if (start != -1 && end != -1 && start < end) {
            return fullHtmlContent.substring(start + 6, end);
        }
        return fullHtmlContent;
    }
}
