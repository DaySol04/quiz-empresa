package com.empresa.quiz_empresa.service;

import com.empresa.quiz_empresa.model.Usuario;
import com.empresa.quiz_empresa.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public void crear(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        repo.save(usuario);
    }

    public void actualizar(Long id, Usuario datos) {
        Usuario existente = buscarPorId(id);
        existente.setUsername(datos.getUsername());
        existente.setRol(datos.getRol());
        
        if (datos.getPassword() != null && !datos.getPassword().trim().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(datos.getPassword()));
        }
        repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}