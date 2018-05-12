package com.refpro.server.DBhandlers;

import com.refpro.server.DTOs.*;
import com.refpro.server.exception.MatchNotFoundException;
import com.refpro.server.models.MatchInfo;

import java.util.List;

public interface MatchInfoService {
    String addMatchInfo(NewMatchInfoDTO newMatchInfoDTO) throws Exception;

    void updateMatchInfo(MatchUpdateDTO matchUpdateDTO) throws MatchNotFoundException;

    void addMatchEventToMatch(String id, MatchEventDTO matchEvent) throws MatchNotFoundException;

    void addPlayerEventToMatch(String id, PlayerEventDTO matchEventDTO) throws MatchNotFoundException;

    List<MatchInfo> getAllMatchInfo();

    public MatchInfoDTO getMatchById(String id) throws MatchNotFoundException;

    void deleteMatchInfo(String id) throws MatchNotFoundException;
}
