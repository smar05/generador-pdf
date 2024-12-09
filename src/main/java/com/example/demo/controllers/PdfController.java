package com.example.demo.controllers;

import com.example.demo.dto.SeguroDTO;
import com.example.demo.entities.SeguroIntegralCancer;
import com.example.demo.repositories.SeguroIntegralCancerRepository;
import com.example.demo.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

import java.util.Base64;
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

    @GetMapping("/generate-pdf-base64-by-id")
    public ResponseEntity<String> generatePdfBase64FromMongo(@RequestParam(value = "idSeguroIntegralCancer") final String idSeguroIntegralCancer) {
        try {
            final Context context = new Context();

            final Optional<SeguroIntegralCancer> optionalSeguroIntegralCancer = seguroIntegralCancerRepository.findById(idSeguroIntegralCancer);
            final SeguroIntegralCancer seguroIntegralCancer = optionalSeguroIntegralCancer.orElse(null);

            if (seguroIntegralCancer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El seguro no fue encontrado");
            }

            context.setVariable("seguro", seguroIntegralCancer);

            final byte[] pdfBytes = pdfService.generatePdf("conditional-template", context);
            final String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(base64EncodedPdf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el PDF: " + e.getMessage());
        }
    }

    @GetMapping("/generate-pdf-base64")
    public ResponseEntity<String> generatePdfBase64(@RequestParam String tomador,
                                                    @RequestParam String tipoDocumento,
                                                    @RequestParam Long numeroDocumento,
                                                    @RequestParam String fechaNacimiento,
                                                    @RequestParam String genero,
                                                    @RequestParam String nacionalidad,
                                                    @RequestParam String pais,
                                                    @RequestParam String email,
                                                    @RequestParam Long celular,
                                                    @RequestParam String direccion,
                                                    @RequestParam String ciudad,
                                                    @RequestParam Long numPoliza,
                                                    @RequestParam Long precioSeguro,
                                                    @RequestParam String fechaInicioVigencia,
                                                    @RequestParam String fechaFinVigencia,
                                                    @RequestParam Long cuantoCubreDiagnostico,
                                                    @RequestParam Long cuantoCubreMuerte) {
        try {
            final Context context = new Context();

            final SeguroDTO seguroDTO = SeguroDTO.builder()
                    .tomador(tomador)
                    .tipoDocumento(tipoDocumento)
                    .numeroDocumento(numeroDocumento)
                    .fechaNacimiento(fechaNacimiento)
                    .genero(genero)
                    .nacionalidad(nacionalidad)
                    .pais(pais)
                    .email(email)
                    .celular(celular)
                    .direccion(direccion)
                    .ciudad(ciudad)
                    .numPoliza(numPoliza)
                    .precioSeguro(precioSeguro)
                    .fechaInicioVigencia(fechaInicioVigencia)
                    .fechaFinVigencia(fechaFinVigencia)
                    .cuantoCubreDiagnostico(cuantoCubreDiagnostico)
                    .cuantoCubreMuerte(cuantoCubreMuerte)
                    .build();

            context.setVariable("seguro", seguroDTO);

            final byte[] pdfBytes = pdfService.generatePdf("conditional-template", context);
            final String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

            /*final HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=generated.pdf");
                headers.add("Content-Type", "application/pdf");*/

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(base64EncodedPdf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el PDF: " + e.getMessage());
        }
    }
}