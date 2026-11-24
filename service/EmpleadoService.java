package com.empresa.quiz.service;

import com.empresa.quiz.model.Empleado;
import com.empresa.quiz.repository.DepartamentoRepository;
import com.empresa.quiz.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final DepartamentoRepository deptoRepo;

    public EmpleadoService(EmpleadoRepository repo, DepartamentoRepository deptoRepo) {
        this.repo = repo;
        this.deptoRepo = deptoRepo;
    }

    public List<Empleado> listar() {
        return repo.findAll();
    }

    public List<Empleado> listarPorDepto(Long deptoId) {
        return repo.findByDepartamentoId(deptoId);
    }

    public Empleado buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
    }

    // 1. Crear
    @Transactional
    public void crear(Empleado empleado) {
        // Regla: Cédula única
        if (repo.existsByCedula(empleado.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        // Regla: Salario no supera el presupuesto
        BigDecimal planillaActual = deptoRepo.sumSalariosByDepartamentoId(empleado.getDepartamento().getId());
        BigDecimal nuevaPlanilla = planillaActual.add(empleado.getSalario());
        
        if (nuevaPlanilla.compareTo(empleado.getDepartamento().getPresupuesto()) > 0) {
            throw new IllegalArgumentException("El salario excede el presupuesto disponible del departamento");
        }

        repo.save(empleado);
    }

    // 2. Actualizar
    @Transactional
    public void actualizar(Long id, Empleado datos) {
        Empleado existente = buscarPorId(id);

        // Regla: Cédula única al editar
        if (repo.existsByCedulaAndIdNot(datos.getCedula(), id)) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        // Regla: Validar presupuesto con el departamento NUEVO
        BigDecimal planillaNuevoDepto = deptoRepo.sumSalariosByDepartamentoId(datos.getDepartamento().getId());
        
        // Si el empleado se queda en el mismo departamento, se resta su salario anterior para no sumarlo dos veces en el cálculo.
        if (existente.getDepartamento().getId().equals(datos.getDepartamento().getId())) {
            planillaNuevoDepto = planillaNuevoDepto.subtract(existente.getSalario());
        }
        
        BigDecimal nuevaPlanilla = planillaNuevoDepto.add(datos.getSalario());
        if (nuevaPlanilla.compareTo(datos.getDepartamento().getPresupuesto()) > 0) {
            throw new IllegalArgumentException("El salario excede el presupuesto disponible del departamento");
        }

        // Regla: Si cambia de departamento, mantener sincronizadas las listas
        if (!existente.getDepartamento().getId().equals(datos.getDepartamento().getId())) {
            existente.getDepartamento().quitarEmpleado(existente);
            datos.getDepartamento().agregarEmpleado(existente);
        }

        // Actualizamos los datos
        existente.setNombre(datos.getNombre());
        existente.setCedula(datos.getCedula());
        existente.setEmail(datos.getEmail());
        existente.setPuesto(datos.getPuesto());
        existente.setSalario(datos.getSalario());
        existente.setFechaIngreso(datos.getFechaIngreso());
        existente.setDepartamento(datos.getDepartamento());

        repo.save(existente);
    }

    // 3. Eliminar
    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}