package tn.esprit.pidev.Services;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
@Service
public class PdfService {

    public byte[] generatePdfFromHtml(String htmlContent) throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        Document document = Jsoup.parse(htmlContent);
        renderer.setDocumentFromString(document.outerHtml());
        renderer.layout();
        renderer.createPDF(outputStream);

        return ((ByteArrayOutputStream) outputStream).toByteArray();

    }

}