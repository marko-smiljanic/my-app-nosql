package com.example.application.services;

import com.example.application.DTO.Konverzija;
import com.example.application.DTO.UserDTO;
import com.example.application.entity.Firma;
import com.example.application.entity.User;
import com.example.application.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

//    public List<UserDTO> findAll() {
//        //return repository.findAll();
//        ArrayList<UserDTO> listadto = new ArrayList<>();
//        for(User n : repository.findAll()){
//            UserDTO ndto = null;
//            ndto = Konverzija.konvertujUDto(n, UserDTO.class);
//            listadto.add(ndto);
//        }
//        return listadto;
//    }

    @Transactional  //ovo je bitno staviti jer program puca jer ne stigne da uveze sa rolama
    public List<UserDTO> findAll2() {
        return repository.findAll().stream()
                .map(user -> Konverzija.konvertujUDto(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public User findByUsername(String username){
        return this.repository.findByUsername(username);
    }

    //za lazy loading mora da se vraca Stream
    @Transactional
    public List<UserDTO> lazyFindAll(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page, pageSize)).stream()
                .map(user -> Konverzija.konvertujUDto(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long userId) {
        return repository.findById(userId)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getIme(), user.getPrezime(), user.getUsername(), user.getProfilePicture()));
    }

    @Transactional
    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public User create(User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
