package com.example.examplefeature.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

@Entity
public class Empleado {

    // nombre, dni, edad, email, titulacion, experiencia

    @Id
    @NotEmpty(message = "El DNI no puede estar vacío")
    @Size(min = 8, max = 9, message = "DNI inválido")
    private String dni;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 3, message = "Mínimo 3 caracteres")
    private String nombre;

    @Email(message = "Debe ser un correo válido")
    @NotEmpty(message = "El email es requerido")
    private String email;

    @NotNull(message = "Indique la edad")
    @Min(value = 18, message = "Edad mínima: 18 años")
    private Integer edad;

    @NotEmpty(message = "La titulación no puede estar vacía")
    @Size(min = 3, message = "Mínimo 3 caracteres")
    private String titulacion;

    @NotEmpty(message = "La experiencia no puede estar vacía")
    @Size(min = 3, message = "Mínimo 3 caracteres")
    private String experiencia;

    // GETTERS Y SETTERS

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getTitulacion() {
        return titulacion;
    }

    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }
}