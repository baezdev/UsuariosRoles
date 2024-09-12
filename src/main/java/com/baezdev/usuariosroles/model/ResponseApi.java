package com.baezdev.usuariosroles.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApi<T> {
    private T result;
    private String message;
    private String status;
}
