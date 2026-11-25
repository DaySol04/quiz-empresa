package com.empresa.quiz_empresa.controller;

import com.empresa.quiz_empresa.model.Usuario;
import com.empresa.quiz_empresa.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", service.listar());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes flash) {
        try {
            service.crear(usuario);
            flash.addFlashAttribute("mensaje", "Usuario creado correctamente");
            return "redirect:/usuarios";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
            return "redirect:/usuarios/nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        try {
            Usuario u = service.buscarPorId(id);
            u.setPassword(""); // No muestra la contraseña encriptada
            model.addAttribute("usuario", u);
            return "usuarios/formulario";
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario, RedirectAttributes flash) {
        try {
            service.actualizar(id, usuario);
            flash.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
            return "redirect:/usuarios";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al actualizar usuario");
            return "redirect:/usuarios/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se puede eliminar el usuario");
        }
        return "redirect:/usuarios";
    }
}