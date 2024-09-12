package com.baezdev.usuariosroles.repository;

import com.baezdev.usuariosroles.model.Rol;
import com.baezdev.usuariosroles.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    Page<User> findByNombre(String nombre, Pageable pageable);
    List<User> findByRoles_Id(String id);
}
