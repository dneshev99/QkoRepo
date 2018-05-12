package com.refpro.server.repositories;

import com.refpro.server.models.MatchInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchInfoRepository extends MongoRepository<MatchInfo,String> {
}
