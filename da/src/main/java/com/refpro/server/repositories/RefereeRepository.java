package com.refpro.server.repositories;

import com.refpro.server.models.Referee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefereeRepository extends MongoRepository<Referee,String> {
}
