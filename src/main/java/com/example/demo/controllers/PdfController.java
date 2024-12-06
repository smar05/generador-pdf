package com.example.demo.controllers;

import com.example.demo.entities.SeguroIntegralCancer;
import com.example.demo.repositories.SeguroIntegralCancerRepository;
import com.example.demo.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class PdfController {

    @Autowired
    private SeguroIntegralCancerRepository seguroIntegralCancerRepository;

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam(value = "idSeguroIntegralCancer", required = false) final String idSeguroIntegralCancer) {
        final Context context = new org.thymeleaf.context.Context();

        if (idSeguroIntegralCancer != null) {
            final Optional<SeguroIntegralCancer> optionalSeguroIntegralCancer = seguroIntegralCancerRepository.findById(idSeguroIntegralCancer);
            final SeguroIntegralCancer seguroIntegralCancer = optionalSeguroIntegralCancer.orElse(null);

            context.setVariable("seguro", seguroIntegralCancer);
        }

        final byte[] pdfBytes = pdfService.generatePdf("conditional-template", context);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=generated.pdf");
        headers.add("Content-Type", "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}