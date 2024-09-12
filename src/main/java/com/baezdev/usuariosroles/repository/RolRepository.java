package com.baezdev.usuariosroles.repository;

import com.baezdev.usuariosroles.model.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RolRepository extends MongoRepository<Rol, String> {
    boolean existsByNombre(String nombre);
    Optional<Rol> findByNombre(String nombre);
}
