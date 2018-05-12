package com.refpro.server.repositories;

import com.refpro.server.models.FirbaseTopics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseTopicRepository extends MongoRepository<FirbaseTopics,String> {
}
