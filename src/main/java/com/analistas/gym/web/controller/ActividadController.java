package com.analistas.gym.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.analistas.gym.model.domain.Actividad;
import com.analistas.gym.model.service.IActividadService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/actividades")
@SessionAttributes("actividades")
public class ActividadController {

    @Autowired
    IActividadService actividadService;

    @GetMapping("/listadoAdmin")
    public String listadoActividades(Model model) {

        model.addAttribute("titulo", "Listado de Actividades");
        model.addAttribute("actividad", actividadService.listarActividades());

        return "actividades/actividades-list-admin";
    }

    @GetMapping("/nuevo")
    public String nuevaActividad(Model model) {

        model.addAttribute("actividad", new Actividad());

        return "actividades/actividades-form";
    }

    // Procesa el formulario
    @PostMapping("/nuevo")
    public String guardarActividad(@RequestParam("nombre") String nombre,
            @RequestParam("monto") Integer monto,
            RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {

        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setMonto(monto);

        actividadService.guardar(actividad);
        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute("mensaje", "Actividad creada exitosamente");
        return "redirect:/actividades/listadoAdmin";
    }

}
