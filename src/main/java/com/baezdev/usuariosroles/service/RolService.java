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
import java.util.Optional;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepo;
    @Autowired
    private UserRepository userRepository;

    public PaginateResponse<Rol> findAllRoles(Pageable pageable) {
        Page<Rol> result = rolRepo.findAll(pageable);
        return new PaginateResponse<>(result.getContent(), result.getTotalPages(), result.getNumber(), result.getNumberOfElements());
    }

    public Rol createRole(Rol rol) throws Exception {
        String currentRol = rol.getNombre().toUpperCase();
        if (rolRepo.existsByNombre(currentRol)) {
            throw new Exception("El rol '" + rol.getNombre() + "' ya existe.");
        }
        rol.setNombre(currentRol);
        return rolRepo.save(rol);
    }

    public Rol findById(String id) {
        return rolRepo.findById(id).orElse(null);
    }

    public Optional<Rol> findByNombre(String nombre) {
        return rolRepo.findByNombre(nombre);
    }

    public boolean deleteRole(String id) {
        if(!rolRepo.existsById(id)) return false;

        rolRepo.deleteById(id);
        return true;
    }

    public Rol updateRole(String id, Rol newRol) {
        if(!rolRepo.existsById(id)) return null;

        newRol.setId(id);
        Rol updatedRol = rolRepo.save(newRol);

        List<User> usersWithRol = userRepository.findByRoles_Id(id);

        for (User user : usersWithRol) {
            List<Rol> roles = user.getRoles();
            for (int i = 0; i < roles.size(); i++) {
                if (roles.get(i).getId().equals(id)) {
                    roles.set(i, updatedRol);
                }
            }
            userRepository.save(user);
        }

        return updatedRol;
    }
}
