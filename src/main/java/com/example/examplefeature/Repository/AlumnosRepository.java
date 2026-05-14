package com.example.examplefeature.Repository;

import com.example.examplefeature.Entity.alumnos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnosRepository extends JpaRepository<alumnos, Long> {}
