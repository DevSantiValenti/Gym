package com.analistas.gym.model.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import groovy.transform.ToString;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer monto;

    @OneToMany(mappedBy = "actividad")
    @JsonIgnore // ←←← Esto evita que se serialice la lista de socios
    private List<Socio> socios;
}
