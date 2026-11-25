package com.empresa.quiz_empresa.repository;

import com.empresa.quiz_empresa.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    // Validaciones de nombre único
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    // Suma de salarios 
    @Query("SELECT COALESCE(SUM(e.salario), 0) FROM Empleado e WHERE e.departamento.id = :deptoId")
    BigDecimal sumSalariosByDepartamentoId(@Param("deptoId") Long deptoId);
}