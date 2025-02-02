package com.example.application.security;

import com.example.application.entity.Role;
import com.example.application.entity.User;
import com.example.application.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//ovo je za direktan pristup trenutno ulogovanom korisniku

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    //vraca trenutno autentifikovanog korisnika iz contexta a zatim koristi injektovani repozitorijum da nadje korisnika iz baze
    @Transactional
    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }


    // Metoda za proveru uloga koristeći enum
    public boolean hasRole(Role role) {
        return get()
                .map(user -> user.getRoles().contains(role))
                .orElse(false);
    }

//    // Metoda za proveru uloga sa prosledjenim stringom
//    public boolean hasRole(String role) {
//        Role roleEnum = convertToRoleEnum(role);
//        return get()
//                .map(user -> user.getRoles().stream()
//                        .anyMatch(r -> r.equals(roleEnum)))
//                .orElse(false);
//    }
//    // Pomoćna metoda za konverziju stringa u enum zbogh konverzije stringa u enum
//    private Role convertToRoleEnum(String role) {
//        try {
//            return Role.valueOf(role.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            return null; // ili baci izuzetak, zavisno od tvoje logike
//        }
//    }


    //Da li ima bar jednu koja je prosledjena
//    public boolean hasAnyRole(Set<Role> roles) {
//        return get()
//                .map(user -> user.getRoles().stream()
//                        .anyMatch(role -> roles.contains(role)))
//                .orElse(false);
//    }

    //Da li ima sve koje su prosledjene
//    public boolean hasAllRoles(Set<Role> requiredRoles) {
//        return get()
//                .map(user -> requiredRoles.stream()
//                        .allMatch(role -> user.getRoles().contains(role)))
//                .orElse(false);
//    }

}
