package com.example.examplefeature.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class DTO {

        @Id
        @NotEmpty(message = "El DNI no puede estar vacío")
        @Size(min = 8, max = 9, message = "DNI inválido")
        private String dni;

}
