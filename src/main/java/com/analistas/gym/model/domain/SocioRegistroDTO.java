package com.analistas.gym.model.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SocioRegistroDTO {

    // --- Paso 1: datos personales ---
    private String nombreCompleto;
    private String dni;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;
    private String actividad;
    private String profesion;
    // private String email;
    // private String comoNosConocio;
    // private String observaciones;

    // --- Paso 2: datos de membresía ---
    private Integer monto;
    // private Integer diasPorSemana;
    // private LocalDate fechaInicio;
    // private String tipoMembresia;

    // getters y setters
}