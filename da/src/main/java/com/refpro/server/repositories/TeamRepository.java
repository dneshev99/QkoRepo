package com.refpro.server.repositories;

import com.refpro.server.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends MongoRepository<Team, String>{

    Team findByName(String teamName);
}
