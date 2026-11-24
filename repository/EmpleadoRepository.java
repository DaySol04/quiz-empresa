package com.empresa.quiz.repository;

import com.empresa.quiz.model.Empleado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    // @EntityGraph optimiza la consulta para traer el departamento de una vez
    @Override 
    @EntityGraph(attributePaths = "departamento")
    List<Empleado> findAll();

    @EntityGraph(attributePaths = "departamento")
    List<Empleado> findByDepartamentoId(Long departamentoId);

    // Validación de cédula única
    boolean existsByCedula(String cedula);
    boolean existsByCedulaAndIdNot(String cedula, Long id);
}