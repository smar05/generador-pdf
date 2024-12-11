package com.example.demo.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(String templateName, Context context) {
        try {
            String htmlContent = templateEngine.process(templateName, context);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            renderer.createPDF(byteArrayOutputStream);
            byteArrayOutputStream.close();

            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }

    public Path createAndValidatePdf(final byte[] pdfBytes, final String fileName) throws IOException {
        final String filePath = "";

        if(!validatePdf(pdfBytes)){
            return null;
        }

        final Path path = Files.write(Paths.get(filePath + fileName + ".pdf"), pdfBytes);

        if (!Files.exists(path)) {
            return null;
        }

        return path;
    }

    public boolean validatePdf(final byte[] pdfBytes) {
        try {
            PDDocument.load(pdfBytes).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean deleteFile(final String path) {
        return new File(path).delete();
    }
}
