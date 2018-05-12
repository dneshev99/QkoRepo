package com.refpro.server.repositories;

import com.refpro.server.models.Player;
import com.refpro.server.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player,String> {
    List<Player> findAllByTeam(Team team);
    Player findPlayerByShirtNumberAndShirtName(int shirtNumber, String shirtName);
    Player findPlayerById(String id);
}
