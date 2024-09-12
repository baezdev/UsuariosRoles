package com.baezdev.usuariosroles.controller;

import com.baezdev.usuariosroles.model.ApiResponse;
import com.baezdev.usuariosroles.model.PaginateResponse;
import com.baezdev.usuariosroles.model.User;
import com.baezdev.usuariosroles.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginateResponse<User> users = userService.findAllUsers(pageable);
            ApiResponse<PaginateResponse<User>> apiResponse = new ApiResponse<>(users, "Usuarios obtenidos con exito", "success");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userService.findUserById(id);

            if (user == null) {
                return new ResponseEntity<>(new ApiResponse<>(null, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponse<>(user, "Usuario encontrado", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getRoleByNombre(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginateResponse<User> users = userService.findUserByName(name, pageable);
            ApiResponse<PaginateResponse<User>> apiResponse = new ApiResponse<>(users, "Usuarios obtenidos con exito", "success");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser (@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user, user.getRoles());

            ApiResponse<User> userResponse = new ApiResponse<>(createdUser, "Usuario creado con exito", "success");
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<String> userResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRoleById(@PathVariable String id) {
        try {
            boolean userIsDeleted = userService.deleteUserById(id);
            if(!userIsDeleted) {
                return new ResponseEntity<>(new ApiResponse<>(false, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponse<>(true, "El usuario fue eliminado exitosamente", "success"), HttpStatus.OK);

        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateRoleById(@PathVariable String id, @Valid @RequestBody User user) {
        try {
            User response = userService.updateUser(user, id);
            if (response == null) {
                return new ResponseEntity<>(new ApiResponse<>(null, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse<>(response, "El usuario se actualizo correctamente", "success"), HttpStatus.OK);
        }
        catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
