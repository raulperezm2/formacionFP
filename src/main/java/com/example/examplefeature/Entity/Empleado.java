package com.example.examplefeature.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 3, message = "Mínimo 3 caracteres")
    private String nombre;

    @NotEmpty(message = "Los apellidos no pueden estar vacío")
    private String apellidos;

    @Email(message = "Debe ser un correo válido")
    @NotEmpty(message = "El email es requerido")
    private String email;

    private Integer numTelefono;

    @NotNull(message = "Indique la edad")
    @Min(value = 18, message = "Edad mínima: 18 años")
    private Integer edad;

    private String genero;

    @NotEmpty(message = "La última titulación puede estar vacía")
    private String ultima_titulacion;

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

    public void setEdad(Integer edad) { this.edad = edad; }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public int getNumTelefono() { return numTelefono; }

    public void setNumTelefono(Integer numTelefono) { this.numTelefono = numTelefono; }

    public String getUltima_titulacion() { return ultima_titulacion; }

    public void setUltima_titulacion(String ultima_titulacion) { this.ultima_titulacion = ultima_titulacion; }

    public String getGenero() { return genero; }

    public void setGenero(String genero) { this.genero = genero; }
}