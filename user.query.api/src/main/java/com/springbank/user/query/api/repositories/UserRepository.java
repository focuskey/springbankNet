package com.springbank.user.query.api.repositories;


import com.springbank.user.core.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

// this function deal with mongo db
public interface UserRepository extends MongoRepository<User, String> {
}
