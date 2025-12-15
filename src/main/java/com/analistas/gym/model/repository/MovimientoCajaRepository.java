package com.analistas.gym.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.analistas.gym.model.domain.MovimientoCaja;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {

    List<MovimientoCaja> findByFechaHoraBetween(
            LocalDateTime desde,
            LocalDateTime hasta
    );
}
