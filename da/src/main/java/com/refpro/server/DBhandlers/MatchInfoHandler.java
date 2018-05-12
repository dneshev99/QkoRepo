package com.refpro.server.DBhandlers;

import com.refpro.server.DTOs.*;
import com.refpro.server.exception.InvalidInputException;
import com.refpro.server.exception.MatchNotFoundException;
import com.refpro.server.models.*;
import com.refpro.server.repositories.MatchInfoRepository;
import com.refpro.server.repositories.PlayerRepository;
import com.refpro.server.repositories.TeamRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchInfoHandler implements MatchInfoService {
    @Autowired
    private MatchInfoRepository matchInfoRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerHandler playerHandler;

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MatchInfoHandler.class.getName());
    @Override
    public String addMatchInfo(NewMatchInfoDTO newMatchInfoDTO) throws Exception {
        MatchInfo newEntry = new MatchInfo();

        newEntry.setActive(newMatchInfoDTO.isActive());
        newEntry.setCompetition(newMatchInfoDTO.getCompetition());
        newEntry.setVenue(newMatchInfoDTO.getVenue());
        newEntry.setDate(newMatchInfoDTO.getDate());
        newEntry.setTime(newMatchInfoDTO.getTime());

        Team homeTeam = teamRepository.findByName(newMatchInfoDTO.getHome());
        Team awayTeam = teamRepository.findByName(newMatchInfoDTO.getAway());
        if(homeTeam==null || awayTeam==null) {
            log.error("HomeTeam : {} , AwayTeam:{}",homeTeam,awayTeam);
            throw new InvalidInputException("Home team or Away team can not be found.");
        }
        newEntry.setHome(homeTeam);

        newEntry.setAway(awayTeam);


        newEntry.setHomeAbbr(homeTeam.getAbbreaviature());
        newEntry.setAwayAbbr(awayTeam.getAbbreaviature());

        newEntry.setLength(newMatchInfoDTO.getHalfLength());

        MatchInfo matchInfo = matchInfoRepository.save(newEntry);


        return matchInfo.getID();

    }

    @Override
    public void updateMatchInfo(MatchUpdateDTO matchUpdateDTO) throws MatchNotFoundException {
            System.out.println(matchUpdateDTO.getMatchId());
            MatchInfo entryToUpdate = matchInfoRepository.findOne(matchUpdateDTO.getMatchId());

            if (entryToUpdate == null) throw new MatchNotFoundException("Match not found");

            List<Player> homePlayers = getPlayersFromDTO(matchUpdateDTO.getPlayersHome());
            List<Player> awayPlayers = getPlayersFromDTO(matchUpdateDTO.getPlayersAway());
            List<Player> homeSubs = getPlayersFromDTO(matchUpdateDTO.getSubsHome());
            List<Player> awaySubs = getPlayersFromDTO(matchUpdateDTO.getSubsAway());

            entryToUpdate.setHomePlayers(homePlayers);
            entryToUpdate.setAwayPlayers(awayPlayers);
            entryToUpdate.setSubsHome(homeSubs);
            entryToUpdate.setSubsAway(awaySubs);

            matchInfoRepository.save(entryToUpdate);
    }

    @Override
    public MatchInfoDTO getMatchById(String id) throws MatchNotFoundException {
        MatchInfoDTO result = new MatchInfoDTO();

        MatchInfo entry = matchInfoRepository.findOne(id);

        if (entry == null) throw new MatchNotFoundException("Match not found");

        Team homeTeam = entry.getHome();
        Team awayTeam = entry.getAway();

        result.setCompetition(entry.getCompetition());
        result.setVenue(entry.getVenue());
        result.setDate(entry.getDate());
        result.setTime(entry.getTime());
        result.setAway(new TeamDto(awayTeam));
        result.setAwayAbbr(awayTeam.getAbbreaviature());
        result.setHome(new TeamDto(homeTeam));
        result.setHomeAbbr(homeTeam.getAbbreaviature());
        result.setAwayPlayers(playerHandler.convertEntitiesToDto(entry.getAwayPlayers()));
        result.setHomePlayers(playerHandler.convertEntitiesToDto(entry.getHomePlayers()));
        result.setLength(entry.getLength());
        result.setSubsAway(playerHandler.convertEntitiesToDto(entry.getSubsAway()));
        result.setSubsHome(playerHandler.convertEntitiesToDto(entry.getSubsHome()));

        return result;

    }

    private List<Player> getPlayersFromDTO(ArrayList<PlayerDTO> playersDTO) {
        List<Player> result = new ArrayList<>();


        for (PlayerDTO playerDTO : playersDTO) {
            Player newEntry = playerRepository.findPlayerByShirtNumberAndShirtName(playerDTO.getShirtNumber(),
                                                                                   playerDTO.getShirtName());
            result.add(newEntry);
        }

        return result;
    }

    @Override
    public void addMatchEventToMatch(String id, MatchEventDTO matchEventDTO) throws MatchNotFoundException {
        MatchInfo matchInfo = matchInfoRepository.findOne(id);
        MatchEvent matchEvent = new MatchEvent();

        if (matchInfo == null) throw new MatchNotFoundException("Match not found");

        matchEvent.setEventType(matchEventDTO.getEventType());
        matchEvent.setTime(matchEventDTO.getTime());
        matchEvent.setMessage(matchEventDTO.getMessage());

        matchInfo.addEvent(matchEvent);

        matchInfoRepository.save(matchInfo);
    }

    @Override
    public void addPlayerEventToMatch(String id, PlayerEventDTO playerEventDTO) throws MatchNotFoundException {
        MatchInfo entry = matchInfoRepository.findOne(id);
        PlayerEvent playerEvent = new PlayerEvent();

        if (entry == null) throw new MatchNotFoundException("Match not found");

        Player player = playerRepository.findPlayerByShirtNumberAndShirtName(playerEventDTO.getPlayer().getShirtNumber(),
                                                                             playerEventDTO.getPlayer().getShirtName());



        playerEvent.setEventType(playerEventDTO.getEventType());
        playerEvent.setTime(playerEventDTO.getTime());
        playerEvent.setMessage(playerEventDTO.getMessage());
        //playerEvent.setPlayer(player);

        entry.addEvent(playerEvent);

        matchInfoRepository.save(entry);

    }

    @Override
    public List<MatchInfo> getAllMatchInfo() {
        return matchInfoRepository.findAll();
    }


    @Override
    public void deleteMatchInfo(String id) throws MatchNotFoundException {
        MatchInfo entry = matchInfoRepository.findOne(id);

        if (entry == null) throw new MatchNotFoundException("Match not found");

        matchInfoRepository.delete(id);
    }
}
