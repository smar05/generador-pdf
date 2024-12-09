package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguroDTO {
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
