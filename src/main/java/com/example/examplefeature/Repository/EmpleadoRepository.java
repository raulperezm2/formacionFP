package com.example.examplefeature.Repository;

import com.example.examplefeature.Entity.Empleado;
import com.example.examplefeature.Entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {}