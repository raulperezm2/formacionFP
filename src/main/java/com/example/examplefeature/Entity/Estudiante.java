package com.example.examplefeature.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Estudiante {
    //EmpleadosEig, nombre, dni, edad, email, titulacion, experiencia.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 3, message = "Mínimo 3 caracteres")
    private String nombre;

    @Email(message = "Debe ser un correo válido")
    @NotEmpty(message = "El email es requerido")
    private String email;

    @NotNull(message = "Indique la edad")
    @Min(value = 18, message = "Edad mínima: 18 años")
    private Integer edad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}