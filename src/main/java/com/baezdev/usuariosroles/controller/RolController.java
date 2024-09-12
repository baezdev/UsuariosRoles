package com.baezdev.usuariosroles.controller;

import com.baezdev.usuariosroles.model.ResponseApi;
import com.baezdev.usuariosroles.model.PaginateResponse;
import com.baezdev.usuariosroles.model.Rol;
import com.baezdev.usuariosroles.service.RolService;
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

import java.util.Optional;

@RestController
@RequestMapping("/roles")
@Validated
public class RolController {
    @Autowired
    private RolService rolService;

    @Operation(summary = "Obtener todos los roles", description = "Devuelve una lista paginada de todos los roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. La respuesta contiene una lista paginada de roles.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Roles obtenidos ejemplo",
                                    value = "{ \"result\": { \"data\": [ { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"GERENTE\" }, { \"id\": \"66e23eaf13025357e2a4e387\", \"nombre\": \"ADMIN\" } ], \"totalPages\": 1, \"currentPage\": 0, \"totalElements\": 2 }, \"message\": \"Roles obtenidos con exito\", \"status\": \"success\" }"))),
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
    public ResponseEntity<ResponseApi<?>> getAllRoles(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginateResponse<Rol> roles = rolService.findAllRoles(pageable);
            ResponseApi<PaginateResponse<Rol>> responseApi = new ResponseApi<>(roles, "Roles obtenidos con exito", "success");
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Obtener un rol por ID", description = "Devuelve los detalles de un rol específico basado en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "El rol no existe"),
            @ApiResponse(responseCode = "500", description = "Error en el servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(
            @Parameter(description = "ID del rol que se desea buscar")
            @PathVariable String id) {
        try {
            Rol rol = rolService.findById(id);

            if (rol == null) {
                return new ResponseEntity<>(new ResponseApi<>(null, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseApi<>(rol, "Rol encontrado", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener un rol por nombre", description = "Devuelve los detalles de un rol específico basado en el nombre proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente. La respuesta contiene un objeto Rol con los detalles del rol.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Rol encontrado ejemplo",
                                    value = "{ \"data\": { \"id\": \"12345\", \"nombre\": \"ADMIN\" }, \"message\": \"Rol encontrado\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "404", description = "El rol no existe. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 404 ejemplo",
                                    value = "{ \"data\": null, \"message\": \"El rol no existe\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"data\": null, \"message\": \"Error interno del servidor\", \"status\": \"error\" }")))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getRoleByNombre(@PathVariable String name) {
        try {
            Optional<Rol> rolOptional = rolService.findByNombre(name.toUpperCase());

            if (rolOptional.isEmpty()) {
                return new ResponseEntity<>(new ResponseApi<>(null, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }

            Rol rol = rolOptional.get();
            return new ResponseEntity<>(new ResponseApi<>(rol, "Rol encontrado", "success"), HttpStatus.OK);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Crear un rol", description = "Crea un nuevo rol basado en los detalles proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente. La respuesta contiene el objeto Rol creado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Rol creado ejemplo",
                                    value = "{ \"data\": { \"id\": \"12345\", \"nombre\": \"Admin\" }, \"message\": \"Rol creado exitosamente\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 400 ejemplo",
                                    value = "{ \"data\": null, \"message\": \"Error en la solicitud\", \"status\": \"error\" }")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Detalles del rol a crear",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Rol.class),
                    examples = @ExampleObject(name = "Rol ejemplo",
                            value = "{ \"nombre\": \"Admin\" }")))
    @PostMapping
    public ResponseEntity<ResponseApi<?>> createRol(@Valid @RequestBody Rol rol) {
        try {
            Rol response = rolService.createRole(rol);
            ResponseApi<Rol> responseApi = new ResponseApi<>(response, "Rol creado exitosamente", "success");
            return new ResponseEntity<>(responseApi, HttpStatus.CREATED);
        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar un rol por ID", description = "Elimina un rol específico basado en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente. La respuesta indica éxito.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Rol eliminado ejemplo",
                                    value = "{ \"result\": true, \"message\": \"El rol fue eliminado exitosamente\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "404", description = "El rol no existe. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 404 ejemplo",
                                    value = "{ \"result\": false, \"message\": \"El rol no existe\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error interno del servidor\", \"status\": \"error\" }")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<?>> deleteRoleById(
            @Parameter(description = "ID del rol que se desea eliminar")
            @PathVariable String id) {
        try {
            boolean rolIsDelete = rolService.deleteRole(id);
            if(!rolIsDelete) {
                return new ResponseEntity<>(new ResponseApi<>(false, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseApi<>(true, "El rol fue eliminado exitosamente", "success"), HttpStatus.OK);

        } catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar un rol por ID", description = "Actualiza un rol específico basado en el ID proporcionado y los nuevos detalles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente. La respuesta contiene el objeto Rol actualizado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Rol actualizado ejemplo",
                                    value = "{ \"result\": { \"id\": \"66e23d6413025357e2a4e386\", \"nombre\": \"ADMIN\" }, \"message\": \"El rol se actualizo exitosamente\", \"status\": \"success\" }"))),
            @ApiResponse(responseCode = "404", description = "El rol no existe. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 404 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"El rol no existe\", \"status\": \"error\" }"))),
            @ApiResponse(responseCode = "500", description = "Error en el servidor. La respuesta contiene un mensaje de error en el cuerpo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseApi.class),
                            examples = @ExampleObject(name = "Error 500 ejemplo",
                                    value = "{ \"result\": null, \"message\": \"Error interno del servidor\", \"status\": \"error\" }")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<?>> updateRoleById(
            @Parameter(description = "ID del rol que se desea actualizar")
            @PathVariable String id, @Valid @RequestBody Rol rol) {
        try {
            Rol response = rolService.updateRole(id, rol);
            if (response == null) {
                return new ResponseEntity<>(new ResponseApi<>(null, "El rol no existe", "error"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ResponseApi<>(response, "El rol se actualizo exitosamente", "success"), HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseApi<String> responseApi = new ResponseApi<>(null, e.getMessage(), "error");
            return new ResponseEntity<>(responseApi, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
