package com.baezdev.usuariosroles.controller;

import com.baezdev.usuariosroles.model.ApiResponse;
import com.baezdev.usuariosroles.model.PaginateResponse;
import com.baezdev.usuariosroles.model.Rol;
import com.baezdev.usuariosroles.service.RolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/roles")
@Validated
public class RolController {
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllRoles(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginateResponse<Rol> roles = rolService.findAllRoles(pageable);
            ApiResponse<PaginateResponse<Rol>> apiResponse = new ApiResponse<>(roles, "Roles obtenidos con exito", "success");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable String id) {
        try {
            Rol rol = rolService.findById(id);

            if (rol == null) {
                return new ResponseEntity<>(new ApiResponse<>(null, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponse<>(rol, "Rol encontrado", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getRoleByNombre(@PathVariable String name) {
        try {
            Optional<Rol> rolOptional = rolService.findByNombre(name.toUpperCase());

            if (rolOptional.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(null, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }

            Rol rol = rolOptional.get();
            return new ResponseEntity<>(new ApiResponse<>(rol, "Rol encontrado", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRol(@Valid @RequestBody Rol rol) {
        try {
            Rol response = rolService.createRole(rol);
            ApiResponse<Rol> apiResponse = new ApiResponse<>(response, "Rol creado exitosamente", "success");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRoleById(@PathVariable String id) {
        try {
            boolean rolIsDelete = rolService.deleteRole(id);
            if(!rolIsDelete) {
                return new ResponseEntity<>(new ApiResponse<>(false, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponse<>(true, "El rol fue eliminado exitosamente", "success"), HttpStatus.OK);

        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateRoleById(@PathVariable String id, @Valid @RequestBody Rol rol) {
        try {
            Rol response = rolService.updateRole(id, rol);
            if (response == null) {
                return new ResponseEntity<>(new ApiResponse<>(null, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse<>(response, "El rol se actualizo exitosamente", "success"), HttpStatus.OK);
        }
        catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
