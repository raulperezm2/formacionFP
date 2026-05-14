package com.example.examplefeature.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "El puesto no puede estar vacío")
    private String puesto;

    @Email(message = "Debe ser un correo válido")
    private String email;

    @NotNull(message = "Indique la edad")
    @Min(18)
    private Integer edad;

    @NotNull(message = "El salario es obligatorio")
    @Min(0)
    private Double salario;

    // GETTERS Y SETTERS (OBLIGATORIO)

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

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
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

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}