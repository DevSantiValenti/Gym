package com.analistas.gym.web.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.analistas.gym.model.domain.MovimientoCaja;
import com.analistas.gym.model.service.MovimientoCajaService;

@Controller
@RequestMapping("/caja")
public class CajaController {

    @Autowired
    private MovimientoCajaService cajaService;

    @GetMapping
    public String verCaja(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model) {
        if (desde == null)
            desde = LocalDate.now();
        if (hasta == null)
            hasta = LocalDate.now();

        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(23, 59, 59);

        List<MovimientoCaja> movimientos = cajaService.findByFechaHoraBetween(inicio, fin);

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("total",
                movimientos.stream().mapToDouble(MovimientoCaja::getMonto).sum());

        return "caja/caja";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMovimiento(@PathVariable Long id) {
        cajaService.eliminar(id);
        return "redirect:/caja";
    }
}
