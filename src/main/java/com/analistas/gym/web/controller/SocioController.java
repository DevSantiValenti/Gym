package com.analistas.gym.web.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.analistas.gym.model.domain.Actividad;
import com.analistas.gym.model.domain.Socio;
import com.analistas.gym.model.domain.SocioRegistroDTO;
import com.analistas.gym.model.service.IActividadService;
import com.analistas.gym.model.service.ISocioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/socios")
@SessionAttributes("socioRegistro")
public class SocioController {

    @Autowired
    ISocioService socioService;

    @Autowired
    IActividadService actividadService;

    @GetMapping("/listadoAdmin")
    public String listadoSocios(Model model) {
        model.addAttribute("titulo", "Listado de Socios");
        model.addAttribute("socios", socioService.listarSociosActualizados());
        return "socios/socios-list-admin.html";
    }

    // Este método se ejecuta automáticamente antes de cualquier handler
    @ModelAttribute("socioRegistro")
    public SocioRegistroDTO inicializarSocioRegistro() {
        return new SocioRegistroDTO();
    }

    // Paso 1
    @GetMapping("/nuevo")
    public String nuevoSocio(Model model) {

        return "socios/socios-form.html";
    }

    // Despues del paso anterior, al dar click en siguiente:

    @PostMapping("/nuevo")
    public String procesarSocio1(@Valid @ModelAttribute("socioRegistro") SocioRegistroDTO dto, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Si hay errores, vuelve al paso 1
            return "socios/socios-form.html";
        }

        System.out.println("DTO recibido: " + dto); // ← Verás si los campos están vacíos

        return "redirect:/socios/nuevo/final";
    }

    // Paso 2: mostrar el formulario de membresía
    @GetMapping("/nuevo/final")
    public String mostrarFormularioPaso2(Model model) {
        // El objeto "socioRegistro" ya está en sesión gracias a @SessionAttributes
        // Si por alguna razón no está, lo agregamos (aunque no debería pasar)

        if (!model.containsAttribute("socioRegistro")) {
            return "redirect:/socios/nuevo";
        }

        List<Actividad> actividades = actividadService.listarActividades();

        model.addAttribute("actividad", actividades);

        model.addAttribute("fechaInicio", LocalDateTime.now());
        model.addAttribute("fechaVencimiento", (LocalDate.now()).plusMonths(1));
        return "socios/socios-form-2.html"; // ← nombre de tu segunda plantilla
    }

    // Paso 2
    @PostMapping("/guardar")
    public String finalizarFormulario(@Valid @ModelAttribute("socioRegistro") SocioRegistroDTO dto,
            BindingResult result, SessionStatus sessionStatus, RedirectAttributes redirectAttributes, Model model) {

        Integer cuota = 20000;

        // -------------------------------------------------------------
        // 1) Ver si el socio YA EXISTE (por DNI)
        // -------------------------------------------------------------
        Socio socioExistente = null;

        if (dto.getDni() != null && !dto.getDni().isBlank()) {
            socioExistente = socioService.buscarPorDNI(dto.getDni());
        }

        // -------------------------------------------------------------
        // 2) SI EXISTE → SOLO ACTUALIZA CUOTA
        // -------------------------------------------------------------
        if (socioExistente != null) {

            socioExistente.setSaldoPendiente(cuota - dto.getMonto());
            socioExistente.setFechaVencimiento(LocalDate.now().plusMonths(1));
            socioExistente.setCuotaPaga(true);

            socioService.guardar(socioExistente);

            redirectAttributes.addFlashAttribute("mensaje", "Cuota abonada con éxito.");
            return "redirect:/home";
        }

        // Si el socio no existe: --------------------------------------------------
        if (result.hasErrors()) {
            return "socios/socios-form.html";
        }

        Socio socio = new Socio();
        // System.out.println("DTO recibido: " + dto); // ← Verás si los campos están
        // vacíos

        // Obtener la actividad:
        Actividad actividad = actividadService.buscarPorId(dto.getActividad().getId());

        socio.setNombreCompleto(dto.getNombreCompleto());
        socio.setDni(dto.getDni());
        socio.setFechaNacimiento(dto.getFechaNacimiento());
        socio.setTelefono(dto.getTelefono());
        socio.setProfesion(dto.getProfesion());
        socio.setDireccion(dto.getDireccion());
        // Paso 2
        socio.setActividad(actividad);
        socio.setFechaAlta(LocalDate.now());
        socio.setFechaVencimiento((LocalDate.now()).plusMonths(1));
        socio.setSaldoPendiente(cuota - dto.getMonto());
        socio.setCuotaPaga(true);

        // model.addAttribute("fechaInicio", socio.getFechaAlta());
        // model.addAttribute("fechaVencimiento", socio.getFechaVencimiento());

        socioService.guardar(socio);
        sessionStatus.setComplete();
        redirectAttributes.addFlashAttribute("mensaje", "Socio Registrado con éxito!");

        return "redirect:/home";
    }

    // Abonar cuota cuando esté vencida:
    @GetMapping("/editar/{id}")
    public String editarEstadoCuota(@PathVariable Long id, Model model) {

        Socio socio = socioService.buscarPorId(id);

        SocioRegistroDTO dto = new SocioRegistroDTO();
        dto.setDni(socio.getDni());

        model.addAttribute("socioRegistro", dto);
        model.addAttribute("titulo", "Abonar Cuota");
        model.addAttribute("socio", socio);
        model.addAttribute("fechaInicio", LocalDateTime.now());
        model.addAttribute("fechaVencimiento", (LocalDate.now()).plusMonths(1));

        return "socios/socios-form-2.html";
    }

    // Eliminar Socio
    @GetMapping("/eliminar/{id}")
    public String eliminarSocio(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Socio socio = socioService.buscarPorId(id);

        if (socio == null) {
            redirectAttributes.addFlashAttribute("error", "El socio no existe.");
            return "redirect:/socios/listadoAdmin";
        }

        socioService.eliminar(id);

        redirectAttributes.addFlashAttribute("mensaje", "Socio eliminado correctamente.");

        return "redirect:/socios/listadoAdmin";
    }

}
