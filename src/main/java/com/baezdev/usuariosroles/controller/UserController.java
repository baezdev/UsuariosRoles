package com.baezdev.usuariosroles.controller;

import com.baezdev.usuariosroles.model.ResponseApi;
import com.baezdev.usuariosroles.model.PaginateResponse;
import com.baezdev.usuariosroles.model.User;
import com.baezdev.usuariosroles.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista paginada de todos los usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. La respuesta contiene una lista paginada de usuarios.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Usuarios obtenidos ejemplo",
                                    value = "{ \"result\": { \"data\": [ { \"id\": \"66e31c05cf04104bd05ce437\", \"nombre\": \"Juan Carlos\", \"a_paterno\": \"Bodoque\", \"a_materno\": \"Treviño\", \"roles\": [ { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"GERENTE\" }, { \"id\": \"66e23eaf13025357e2a4e387\", \"nombre\": \"ADMIN\" } ] } ], \"totalPages\": 1, \"currentPage\": 0, \"totalElements\": 3 }, \"message\": \"Usuarios obtenidos con exito\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 400 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Solicitud incorrecta\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error en el servidor\", \"status\": \"error\" }")))
    })
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

    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve los detalles de un usuario específico basado en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente. La respuesta contiene los detalles del usuario.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Usuario encontrado ejemplo",
                                    value = "{ \"result\": { \"id\": \"66e2a9bda8885e49c9e3d9c8\", \"nombre\": \"Juanin\", \"a_paterno\": \"Lopez\", \"a_materno\": \"Diaz\", \"roles\": [ { \"id\": \"66e23eaf13025357e2a4e387\", \"nombre\": \"ADMIN\" }, { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"GERENTE\" } ] }, \"message\": \"Usuario encontrado\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "404", description = "El usuario no existe. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 404 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"El usuario no existe\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error interno del servidor\", \"status\": \"error\" }")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "ID del usuario que se desea buscar")
            @PathVariable String id) {
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

    @Operation(summary = "Obtener usuarios por nombre de rol", description = "Devuelve una lista paginada de usuarios cuyo nombre coincide con el nombre del rol proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente. La respuesta contiene una lista paginada de usuarios.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Usuarios obtenidos ejemplo",
                                    value = "{ \"result\": { \"data\": [ { \"id\": \"66e2a9bda8885e49c9e3d9c8\", \"nombre\": \"Juanin\", \"a_paterno\": \"Lopez\", \"a_materno\": \"Diaz\", \"roles\": [ { \"id\": \"66e23eaf13025357e2a4e387\", \"nombre\": \"ADMIN\" }, { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"GERENTE\" } ] } ], \"totalPages\": 1, \"currentPage\": 0, \"totalElements\": 1 }, \"message\": \"Usuarios obtenidos con exito\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error en el servidor\", \"status\": \"error\" }")))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getRoleByNombre(
            @Parameter(description = "Nombre del rol para buscar usuarios")
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

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente. La respuesta contiene los detalles del usuario creado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Usuario creado ejemplo",
                                    value = "{ \"result\": { \"id\": \"66e31c05cf04104bd05ce437\", \"nombre\": \"Juan Carlos\", \"a_paterno\": \"Bodoque\", \"a_materno\": \"Diaz\", \"roles\": [ { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"GERENTE\" } ] }, \"message\": \"Usuario creado con exito\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 400 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Datos inválidos o faltantes\", \"status\": \"error\" }")))
    })
    @PostMapping
    public ResponseEntity<ResponseApi<?>> createUser(
            @Parameter(description = "Datos del nuevo usuario a crear")
            @Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user, user.getRoles());

            ResponseApi<User> userResponse = new ResponseApi<>(createdUser, "Usuario creado con exito", "success");
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ResponseApi<String> userResponse = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar un usuario por ID", description = "Elimina un usuario específico basado en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Usuario eliminado ejemplo",
                                    value = "{ \"result\": true, \"message\": \"El usuario fue eliminado exitosamente\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "404", description = "El usuario no existe. La respuesta contiene un mensaje de error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 404 ejemplo",
                                    value = "{ \"result\": false, \"message\": \"El usuario no existe\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error en el servidor\", \"status\": \"error\" }")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<?>> deleteRoleById(
            @Parameter(description = "ID del usuario que se desea eliminar")
            @PathVariable String id) {
        try {
            boolean userIsDeleted = userService.deleteUserById(id);
            if (!userIsDeleted) {
                return new ResponseEntity<>(new ResponseApi<>(false, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseApi<>(true, "El usuario fue eliminado exitosamente", "success"), HttpStatus.OK);

        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar un usuario por ID", description = "Actualiza los detalles de un usuario específico basado en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente. La respuesta contiene los detalles del usuario actualizado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Usuario actualizado ejemplo",
                                    value = "{ \"result\": { \"id\": \"66e31c05cf04104bd05ce437\", \"nombre\": \"Juan Carlos\", \"a_paterno\": \"Bodoque\", \"a_materno\": \"Diaz\", \"roles\": [ { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"GERENTE\" } ] }, \"message\": \"El usuario se actualizo correctamente\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "404", description = "El usuario no existe. La respuesta contiene un mensaje de error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 404 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"El usuario no existe\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error en el servidor\", \"status\": \"error\" }")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<?>> updateRoleById(
            @Parameter(description = "ID del usuario que se desea actualizar")
            @PathVariable String id, @Valid @RequestBody User user) {
        try {
            User response = userService.updateUser(user, id);
            if (response == null) {
                return new ResponseEntity<>(new ResponseApi<>(null, "El usuario no existe", "error"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ResponseApi<>(response, "El usuario se actualizo correctamente", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
