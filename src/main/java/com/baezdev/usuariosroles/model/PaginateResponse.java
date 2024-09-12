package com.baezdev.usuariosroles.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginateResponse<T> {
    private List<T> data;
    private int totalPages;
    private int currentPage;
    private int totalElements;
}
