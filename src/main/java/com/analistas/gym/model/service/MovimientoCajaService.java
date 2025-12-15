package com.analistas.gym.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analistas.gym.model.domain.MovimientoCaja;
import com.analistas.gym.model.repository.MovimientoCajaRepository;

@Service
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository repository;

    // MÉTODO AGREGADO PARA QUE EL CONTROLADOR LO PUEDA USAR
    public List<MovimientoCaja> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByFechaHoraBetween(inicio, fin);
    }
    public List<MovimientoCaja> obtenerMovimientosDelDia(LocalDate fecha) {
        LocalDateTime desde = fecha.atStartOfDay();
        LocalDateTime hasta = fecha.atTime(23, 59, 59);

        return repository.findByFechaHoraBetween(desde, hasta);
    }

    public void guardar(MovimientoCaja movimiento) {
        movimiento.setFechaHora(LocalDateTime.now());
        repository.save(movimiento);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public Double calcularTotal(List<MovimientoCaja> movimientos) {
        return movimientos.stream()
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();
    }
}
