package com.baezdev.usuariosroles.service;

import com.baezdev.usuariosroles.model.PaginateResponse;
import com.baezdev.usuariosroles.model.Rol;
import com.baezdev.usuariosroles.model.User;
import com.baezdev.usuariosroles.repository.RolRepository;
import com.baezdev.usuariosroles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolRepository rolRepository;

    public PaginateResponse<User> findAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return new PaginateResponse<>(users.getContent(), users.getTotalPages(), users.getNumber(), users.getNumberOfElements());
    }

    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public PaginateResponse<User> findUserByName(String nombre, Pageable pageable) {
        Page<User> users = userRepository.findByNombre(nombre, pageable);
        return new PaginateResponse<>(users.getContent(), users.getTotalPages(), users.getNumber(), users.getNumberOfElements());
    }

    public User createUser(User user, List<Rol> nameRoles) {
        List<Rol> roles = nameRoles.stream()
                .map(rol -> rolRepository.findByNombre(rol.getNombre())
                        .orElseThrow(() -> new RuntimeException("El rol " + rol.getNombre() + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public boolean deleteUserById(String id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    public User updateUser(User user, String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existingUser.setId(existingUser.getId());
        existingUser.setNombre(user.getNombre());
        existingUser.setA_materno(user.getA_materno());
        existingUser.setA_paterno(user.getA_paterno());

        List<Rol> updatedRoles = user.getRoles().stream()
                .map(rol -> rolRepository.findByNombre(rol.getNombre())
                        .orElseThrow(() -> new RuntimeException("El rol " + rol.getNombre() + " no existe.")))
                .collect(Collectors.toList());

        existingUser.setRoles(updatedRoles);

        return userRepository.save(existingUser);
    }
}
