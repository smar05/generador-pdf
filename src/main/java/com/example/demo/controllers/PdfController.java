package com.example.demo.controllers;

import com.example.demo.services.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

@Controller
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam(value = "showSection1", defaultValue = "true") boolean showSection1,
                                              @RequestParam(value = "showSection2", defaultValue = "false") boolean showSection2,
                                              @RequestParam(value = "showSection3", defaultValue = "true") boolean showSection3) {
        Context context = new org.thymeleaf.context.Context();

        context.setVariable("showSection1", showSection1);
        context.setVariable("showSection2", showSection2);
        context.setVariable("showSection3", showSection3);

        byte[] pdfBytes = pdfService.generatePdf("conditional-template", context);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=generated.pdf");
        headers.add("Content-Type", "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}