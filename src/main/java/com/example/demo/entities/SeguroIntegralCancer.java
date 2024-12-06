package com.example.demo.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "seguro_integral_cancer")
public class SeguroIntegralCancer {

    @Id
    private String id;
    private String tomador;
    private String tipoDocumento;
    private Long numeroDocumento;
    private String fechaNacimiento;
    private String genero;
    private String nacionalidad;
    private String pais;
    private String email;
    private Long celular;
    private String direccion;
    private String ciudad;
    private Long numPoliza;
    private Long precioSeguro;
    private String fechaInicioVigencia;
    private String fechaFinVigencia;
    private Long cuantoCubreDiagnostico;
    private Long cuantoCubreMuerte;
}
