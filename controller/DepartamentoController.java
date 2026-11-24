package com.empresa.quiz.controller;

import com.empresa.quiz.model.Departamento;
import com.empresa.quiz.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    // 1. Listar
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("departamentos", service.listar());
        return "departamentos/lista";
    }

    // 2. Formulario de nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("departamento", new Departamento());
        return "departamentos/formulario";
    }

    // 3. Guardar 
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("departamento") Departamento departamento,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        // Si hay errores de validación vuelve al formulario
        if (errores.hasErrors()) {
            return "departamentos/formulario";
        }
        try {
            service.crear(departamento);
            flash.addFlashAttribute("mensaje", "Departamento creado exitosamente");
            return "redirect:/departamentos"; 
        } catch (IllegalArgumentException e) {
            
            model.addAttribute("error", e.getMessage());
            return "departamentos/formulario";
        }
    }

    // 4. Formulario de edición
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("departamento", service.buscarPorId(id));
            return "departamentos/formulario";
        } catch (IllegalArgumentException e) {
            // Si el ID no existe, da un mensaje y luego redirige a la lista
            flash.addFlashAttribute("error", "El departamento solicitado no existe.");
            return "redirect:/departamentos";
        }
    }

    // 5. Actualizar
    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("departamento") Departamento departamento,
                             BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return "departamentos/formulario";
        }
        try {
            service.actualizar(id, departamento);
            flash.addFlashAttribute("mensaje", "Departamento actualizado correctamente");
            return "redirect:/departamentos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "departamentos/formulario";
        }
    }

    // 6. Eliminar
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("mensaje", "Departamento eliminado correctamente");
        } catch (IllegalArgumentException e) {
            // Alerta roja en la lista si tiene empleados
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/departamentos";
    }
}