package com.raphael.easywaybank.repository;

import com.musdon.thejavaacademybank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}

// bind the repository to a particular entity
// bind the repository to the User entity
// This is saying help me manipulate the user entity in the db whose primary key is of type Long.

// dto - data transfer object
// Helps in collecting data from the user
// You don't want to expose the entities to the users, just expose only the required things. A dto helps you to do that
// Helps separate the object from the entity

// custom response
// We should have a single response for every single service.
// No matter the endpoint you call,you should always have a uniform response format


// N Tier Architecture
// the controller talks to the service and the service talks to the repository which in turn talks to the service the database
// the controller is what we're going to be exposing to clients. It is responsible for accepting incoming http requests from web clients (browsers, postman, etc) and sending custom responses back to the client
// the service is responsible for business logic
// the repository allows us to perform database operations

// the utility package contains reusable classes, constants and methods