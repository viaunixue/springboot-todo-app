package com.chzzk.study.learn_spring_boot.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// @RestController
public class UserResource {

    private UserDaoService service;

    public UserResource(UserDaoService service) {
        this.service = service;
    }

    // GET /users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // GET /users/{id}

    // EntityModel
    // WebMvcLinkBuilder
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException("id:"+ id);
        }

        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link =linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    // DELETE /users/{id}
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteById(id);
    }

    // POST /users
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);

        // location - /users/4
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
