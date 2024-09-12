package com.baezdev.usuariosroles.controller;

import com.baezdev.usuariosroles.model.ResponseApi;
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
    public ResponseEntity<ResponseApi<?>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginateResponse<User> users = userService.findAllUsers(pageable);
            ResponseApi<PaginateResponse<User>> responseApi = new ResponseApi<>(users, "Usuarios obtenidos con exito", "success");
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userService.findUserById(id);

            if (user == null) {
                return new ResponseEntity<>(new ResponseApi<>(null, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseApi<>(user, "Usuario encontrado", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
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
            ResponseApi<PaginateResponse<User>> responseApi = new ResponseApi<>(users, "Usuarios obtenidos con exito", "success");
            return new ResponseEntity<>(responseApi, HttpStatus.OK);

        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseApi<?>> createUser (@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user, user.getRoles());

            ResponseApi<User> userResponse = new ResponseApi<>(createdUser, "Usuario creado con exito", "success");
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ResponseApi<String> userResponse = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<?>> deleteRoleById(@PathVariable String id) {
        try {
            boolean userIsDeleted = userService.deleteUserById(id);
            if(!userIsDeleted) {
                return new ResponseEntity<>(new ResponseApi<>(false, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseApi<>(true, "El usuario fue eliminado exitosamente", "success"), HttpStatus.OK);

        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<?>> updateRoleById(@PathVariable String id, @Valid @RequestBody User user) {
        try {
            User response = userService.updateUser(user, id);
            if (response == null) {
                return new ResponseEntity<>(new ResponseApi<>(null, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ResponseApi<>(response, "El usuario se actualizo correctamente", "success"), HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
