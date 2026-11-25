package com.empresa.quiz_empresa.controller;

import com.empresa.quiz_empresa.model.Empleado;
import com.empresa.quiz_empresa.model.Puesto;
import com.empresa.quiz_empresa.repository.DepartamentoRepository;
import com.empresa.quiz_empresa.service.EmpleadoService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService service;
    private final DepartamentoRepository deptoRepo;

    public EmpleadoController(EmpleadoService service, DepartamentoRepository deptoRepo) {
        this.service = service;
        this.deptoRepo = deptoRepo;
    }

    // 1. Listar 
    @GetMapping
    public String listar(@RequestParam(required = false) Long departamentoId, Model model) {
        if (departamentoId != null) {
            model.addAttribute("empleados", service.listarPorDepto(departamentoId));
            model.addAttribute("departamentoSeleccionado", departamentoId);
        } else {
            model.addAttribute("empleados", service.listar());
        }
        
        model.addAttribute("departamentos", deptoRepo.findAll());
        return "empleados/lista";
    }

    // 2. Formulario nuevo 
    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long departamentoId, Model model) {
        Empleado empleado = new Empleado();
        if (departamentoId != null) {
            deptoRepo.findById(departamentoId).ifPresent(empleado::setDepartamento);
        }
        model.addAttribute("empleado", empleado);
        model.addAttribute("departamentos", deptoRepo.findAll());
        model.addAttribute("puestos", Puesto.values());
        return "empleados/formulario";
    }

    // 3. Guardar 
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empleado") Empleado empleado,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("departamentos", deptoRepo.findAll());
            model.addAttribute("puestos", Puesto.values());
            return "empleados/formulario";
        }
        try {
            service.crear(empleado);
            flash.addFlashAttribute("mensaje", "Empleado creado correctamente");
            return "redirect:/empleados";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departamentos", deptoRepo.findAll());
            model.addAttribute("puestos", Puesto.values());
            return "empleados/formulario";
        }
    }

    // 4. Formulario de edición
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("empleado", service.buscarPorId(id));
            model.addAttribute("departamentos", deptoRepo.findAll());
            model.addAttribute("puestos", Puesto.values());
            return "empleados/formulario";
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", "El empleado solicitado no existe.");
            return "redirect:/empleados";
        }
    }

    // 5. Actualizar
    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("empleado") Empleado empleado,
                             BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("departamentos", deptoRepo.findAll());
            model.addAttribute("puestos", Puesto.values());
            return "empleados/formulario";
        }
        try {
            service.actualizar(id, empleado);
            flash.addFlashAttribute("mensaje", "Empleado actualizado correctamente");
            return "redirect:/empleados";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departamentos", deptoRepo.findAll());
            model.addAttribute("puestos", Puesto.values());
            return "empleados/formulario";
        }
    }

    // 6. Eliminar
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("mensaje", "Empleado eliminado correctamente");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/empleados";
    }
}