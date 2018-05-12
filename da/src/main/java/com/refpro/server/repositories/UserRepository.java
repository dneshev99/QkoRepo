package com.refpro.server.repositories;

import com.refpro.server.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String>{
    User getUserByUsername(String username);
}
