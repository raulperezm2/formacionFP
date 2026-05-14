package com.example.examplefeature.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class EmpleadoEig {
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

    @NotEmpty(message = "El primer idioma no puede estar vacío")
    @Size(min = 2)
    private String primerIdioma;
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "El nivel debe estar seleccionado")
    private NivelIdioma nivelPrimerIdioma;

    private String segundoIdioma;
    @Enumerated(EnumType.STRING)
    private NivelIdioma nivelSegundoIdioma;

    @NotEmpty(message = "El título no puede estar vacío")
    @Size(min = 2)
    private String titulacionSuperior;

        public NivelEducativo getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(NivelEducativo nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    @Enumerated(EnumType.STRING)
    private NivelEducativo nivelEducativo;

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

    public String getPrimerIdioma() {
        return primerIdioma;
    }

    public void setPrimerIdioma(String primerIdioma) {
        this.primerIdioma = primerIdioma;
    }

    public NivelIdioma getNivelPrimerIdioma() {
        return nivelPrimerIdioma;
    }

    public void setNivelPrimerIdioma(NivelIdioma nivelPrimerIdioma) {
        this.nivelPrimerIdioma = nivelPrimerIdioma;
    }

    public String getSegundoIdioma() {
        return segundoIdioma;
    }

    public void setSegundoIdioma(String segundoIdioma) {
        this.segundoIdioma = segundoIdioma;
    }

    public NivelIdioma getNivelSegundoIdioma() {
        return nivelSegundoIdioma;
    }

    public void setNivelSegundoIdioma(NivelIdioma nivelSegundoIdioma) {
        this.nivelSegundoIdioma = nivelSegundoIdioma;
    }

    public String getTitulacionSuperior() {
        return titulacionSuperior;
    }

    public void setTitulacionSuperior(String titulacionSuperior) {
        this.titulacionSuperior = titulacionSuperior;
    }
}
