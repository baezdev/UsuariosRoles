package com.baezdev.usuariosroles.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El a_paterno es obligatorio")
    private String a_paterno;
    @NotBlank(message = "El a_materno es obligatorio")
    private String a_materno;
    @NotEmpty(message = "Los roles no pueden estar vacios")
    private List<@Valid Rol> roles;
}
