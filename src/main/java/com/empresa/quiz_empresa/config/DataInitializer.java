package com.empresa.quiz_empresa.config;

import com.empresa.quiz_empresa.model.*;
import com.empresa.quiz_empresa.service.*;
import com.empresa.quiz_empresa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {
    @Bean 
    CommandLineRunner init(UsuarioService us, DepartamentoService ds, EmpleadoService es, UsuarioRepository ur) {
        return args -> {
            if (ur.count() == 0) {
                //  usuarios
                us.crear(new Usuario("admin", "admin123", "ADMIN"));
                us.crear(new Usuario("user", "user123", "USER"));

                // departamentos 
                Departamento d1 = new Departamento("Tecnología", "Piso 1", new BigDecimal("50000"));
                Departamento d2 = new Departamento("Recursos Humanos", "Piso 2", new BigDecimal("30000"));
                ds.crear(d1); ds.crear(d2);

                // Los empleados usando el servicio para que validen el presupuesto
                Empleado e1 = new Empleado(); e1.setNombre("Ana Gómez"); e1.setCedula("111111111"); e1.setEmail("ana@test.com"); e1.setPuesto(Puesto.ANALISTA); e1.setSalario(new BigDecimal("5000")); e1.setFechaIngreso(LocalDate.now().minusMonths(6)); e1.setDepartamento(d1); es.crear(e1);
                
                Empleado e2 = new Empleado(); e2.setNombre("Luis Pérez"); e2.setCedula("222222222"); e2.setEmail("luis@test.com"); e2.setPuesto(Puesto.DESARROLLADOR); e2.setSalario(new BigDecimal("6000")); e2.setFechaIngreso(LocalDate.now().minusMonths(3)); e2.setDepartamento(d1); es.crear(e2);
                
                Empleado e3 = new Empleado(); e3.setNombre("Marta Ruiz"); e3.setCedula("333333333"); e3.setEmail("marta@test.com"); e3.setPuesto(Puesto.JEFATURA); e3.setSalario(new BigDecimal("8000")); e3.setFechaIngreso(LocalDate.now().minusYears(1)); e3.setDepartamento(d1); es.crear(e3);
                
                Empleado e4 = new Empleado(); e4.setNombre("Pedro Solano"); e4.setCedula("444444444"); e4.setEmail("pedro@test.com"); e4.setPuesto(Puesto.SOPORTE); e4.setSalario(new BigDecimal("4000")); e4.setFechaIngreso(LocalDate.now().minusWeeks(2)); e4.setDepartamento(d2); es.crear(e4);
                
                Empleado e5 = new Empleado(); e5.setNombre("Lucía Mora"); e5.setCedula("555555555"); e5.setEmail("lucia@test.com"); e5.setPuesto(Puesto.ANALISTA); e5.setSalario(new BigDecimal("4500")); e5.setFechaIngreso(LocalDate.now().minusDays(10)); e5.setDepartamento(d2); es.crear(e5);
                
                System.out.println("✅ Datos iniciales cargados (Usuarios, Departamentos y Empleados)");
            }
        };
    }
}
