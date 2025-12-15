package com.analistas.gym.model.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimientos_caja")
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actividad;

    private String socioNombreCompleto;

    private String detalle; // Pago de cuota / Inscripción

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago; // EFECTIVO / TRANSFERENCIA

    private Integer monto;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento; // CUOTA / INSCRIPCION

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getSocioNombreCompleto() {
        return socioNombreCompleto;
    }

    public void setSocioNombreCompleto(String socioNombreCompleto) {
        this.socioNombreCompleto = socioNombreCompleto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }

}
