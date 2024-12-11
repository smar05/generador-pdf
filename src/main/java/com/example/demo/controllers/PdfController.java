package com.example.demo.controllers;

import com.example.demo.dto.SeguroDTO;
import com.example.demo.entities.SeguroIntegralCancer;
import com.example.demo.repositories.SeguroIntegralCancerRepository;
import com.example.demo.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Base64;
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
    public ResponseEntity<InputStreamResource> generatePdfBase64FromMongo(@RequestParam(value = "idSeguroIntegralCancer") final String idSeguroIntegralCancer) {
        final Long beginTime = System.nanoTime();
        try {
            final Context context = new Context();

            final Optional<SeguroIntegralCancer> optionalSeguroIntegralCancer = seguroIntegralCancerRepository.findById(idSeguroIntegralCancer);
            final SeguroIntegralCancer seguroIntegralCancer = optionalSeguroIntegralCancer.orElse(null);

            if (seguroIntegralCancer == null) {
                return ResponseEntity.internalServerError().build();
            }

            final Long creacionPdf = System.nanoTime();
            System.out.println("Base de datos: " + String.valueOf((creacionPdf - beginTime)/1000000));

            context.setVariable("seguro", seguroIntegralCancer);

            // Generar el pdf a apartir de la plantilla
            final byte[] pdfBytes = pdfService.generatePdf("conditional-template", context);

            System.out.println("Creacion pdf: " + String.valueOf((System.nanoTime() - creacionPdf)/1000000));

            /*// Se pasa a base 64
            final String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

            final Long creacionValidacion = System.nanoTime();
            System.out.println("base 64: " + String.valueOf((base64 - creacionValidacion)/1000000));*/


            // Creacion y validacion de la generacion del pdf
            final Long initValidate = System.nanoTime();
            //final Path pathPdf = pdfService.createAndValidatePdfFromBase64(base64EncodedPdf, "generated");
            final Path pathPdf = pdfService.createAndValidatePdf(pdfBytes, "generated");

            if(Objects.isNull(pathPdf)) {
                return ResponseEntity.internalServerError().build();
            }

            pdfService.deleteFile(pathPdf.toString());

            System.out.println("Validacion pdf: " + String.valueOf((System.nanoTime() - initValidate)/1000000));

            final ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
            final HttpHeaders headers = new HttpHeaders();

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=generated.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/generate-pdf-base64")
    public ResponseEntity<InputStreamResource> generatePdfBase64(@RequestParam String tomador,
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
        final Long beginTime = System.nanoTime();
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

            // Generar el pdf a apartir de la plantilla
            final byte[] pdfBytes = pdfService.generatePdf("conditional-template", context);

            final Long validacion = System.nanoTime();
            System.out.println("Creacion pdf: " + String.valueOf((validacion - beginTime)/1000000));

            // Se pasa a base 64
            //final String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);


            // Creacion y validacion de la generacion del pdf
            final Path pathPdf = pdfService.createAndValidatePdf(pdfBytes, "generated");

            if(Objects.isNull(pathPdf)) {
                return ResponseEntity.internalServerError().build();
            }

            pdfService.deleteFile(pathPdf.toString());

            System.out.println("Validacion: " + String.valueOf((System.nanoTime() - validacion)/1000000));

            final ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
            final HttpHeaders headers = new HttpHeaders();

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=generated.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}