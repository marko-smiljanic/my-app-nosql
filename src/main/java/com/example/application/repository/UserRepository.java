package com.example.application.repository;


import com.example.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//@Repository  //nije potrebna anotacija zato sto se implementira jpa repository, automatski ga prepoznaje kao repozitorijum
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);
//    User findAllUsers();
}
