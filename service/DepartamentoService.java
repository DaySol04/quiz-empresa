package com.empresa.quiz.service;

import com.empresa.quiz.model.Departamento;
import com.empresa.quiz.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository repo;

    public DepartamentoService(DepartamentoRepository repo) {
        this.repo = repo;
    }

    // 1. Listar todos
    public List<Departamento> listar() {
        return repo.findAll();
    }

    // 2. Buscar por ID
    public Departamento buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));
    }

    // 3. Crear
    @Transactional
    public void crear(Departamento departamento) {
        // Regla: El nombre no puede repetirse
        if (repo.existsByNombreIgnoreCase(departamento.getNombre())) {
            throw new IllegalArgumentException("Ya existe un departamento con ese nombre");
        }
        repo.save(departamento);
    }

    // 4. Actualizar
    @Transactional
    public void actualizar(Long id, Departamento datos) {
        Departamento existente = buscarPorId(id);

        // Regla: Nombre único al editar
        if (repo.existsByNombreIgnoreCaseAndIdNot(datos.getNombre(), id)) {
            throw new IllegalArgumentException("Ya existe un departamento con ese nombre");
        }

        // Regla: Presupuesto no puede bajar de la planilla actual
        BigDecimal planillaActual = repo.sumSalariosByDepartamentoId(id);
        if (datos.getPresupuesto().compareTo(planillaActual) < 0) {
            throw new IllegalArgumentException("El presupuesto no puede ser menor a la planilla actual");
        }

        existente.setNombre(datos.getNombre());
        existente.setUbicacion(datos.getUbicacion());
        existente.setPresupuesto(datos.getPresupuesto());
        repo.save(existente);
    }

    // 5. Eliminar
    @Transactional
    public void eliminar(Long id) {
        Departamento depto = buscarPorId(id);
        
        // No eliminar si tiene empleados
        if (!depto.getEmpleados().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar un departamento con empleados asignados");
        }
        repo.delete(depto);
    }
}