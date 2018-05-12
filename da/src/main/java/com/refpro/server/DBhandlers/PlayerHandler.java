package com.refpro.server.DBhandlers;


import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.refpro.server.DTOs.PlayerDTO;
import com.refpro.server.exception.AbstractRestException;
import com.refpro.server.exception.InvalidInputException;
import com.refpro.server.exception.PlayerNotFoundException;
import com.refpro.server.models.Player;
import com.refpro.server.models.Team;
import com.refpro.server.repositories.PlayerRepository;
import com.refpro.server.repositories.TeamRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Component
public class PlayerHandler implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private Validator validator;
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PlayerHandler.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public static final String PLAYERS_ICONS_BUCKET_NAME = "playersIcons";

    @Override
    public List<String> createPlayer(List<PlayerDTO> players ) throws AbstractRestException{
        List<String> createdIds = new LinkedList<>();
        if(players==null)
            return  null;

        for(PlayerDTO playerDTO :players){

            Set<ConstraintViolation<PlayerDTO>> validationResult = validator.validate(playerDTO);
            if(validationResult.isEmpty()) {
                Player newPlayer = new Player();
                newPlayer.setShirtName(playerDTO.getShirtName());
                newPlayer.setFirstName(playerDTO.getFirstName());
                newPlayer.setLastName(playerDTO.getLastName());
                newPlayer.setBirthday(playerDTO.getBirthday());
                newPlayer.setShirtNumber(playerDTO.getShirtNumber());

                Team team = teamRepository.findByName(playerDTO.getTeam());
                newPlayer.setTeam(team);
                playerRepository.save(newPlayer);
                createdIds.add(newPlayer.getId());
            }else{
                log.debug("Validation error : {}",validationResult.stream().map(s->s.getMessage()).toArray() );

            }
        }
        return createdIds;
    }

    @Override
    public List<PlayerDTO> getPlayersByTeam(String name) throws Exception {
        List<PlayerDTO> result = new ArrayList<>();

        Team team = teamRepository.findByName(name);
        List<Player> players = playerRepository.findAllByTeam(team);
        log.info(players.size()+"");
        for (Player player : players) {
            PlayerDTO newPlayer = new PlayerDTO(player);
            result.add(newPlayer);
        }

        return result;
    }

    List<PlayerDTO> convertEntitiesToDto(List<Player> players){
        List<PlayerDTO> result = new ArrayList<>();
        for (Player player : players) {
            PlayerDTO newPlayer = new PlayerDTO(player);
            result.add(newPlayer);
        }
        return result;
    }

    @Override
    public void addPlayerIcon(String playerId, MultipartFile file) throws IOException, AbstractRestException {

        if(StringUtils.isBlank(playerId)){
            throw new InvalidInputException("Player id can not be null or empty!");
        }

        Player player = playerRepository.findPlayerById(playerId);

        if (player == null) {
            throw new PlayerNotFoundException("Player not found.");
        }
        GridFS gfsPhoto = new GridFS(mongoTemplate.getDb(), PLAYERS_ICONS_BUCKET_NAME);
        GridFSInputFile gfsFile = gfsPhoto.createFile(file.getBytes());
        gfsFile.setChunkSize(4096*1024);
        ObjectId id = new ObjectId();
        gfsFile.setFilename(id.toString());
        gfsFile.setId(id);
        gfsFile.save();

        player.setPictureId(gfsFile.getId().toString());
        playerRepository.save(player);

    }

    @Override
    public ByteArrayResource getPlayerIcon(String playerId) throws AbstractRestException,IOException{
        if(StringUtils.isBlank(playerId)){
            throw new InvalidInputException("Player id can not be null or empty!");
        }
        log.debug("playerId:{}",playerId);
        Player player = playerRepository.findPlayerById(playerId);

        if (player == null) {
            throw new PlayerNotFoundException("Player not found.");
        }

        String pictureId = player.getPictureId();

        if(player.getPictureId()!=null){
            GridFS gfsPhoto = new GridFS(mongoTemplate.getDb(), PLAYERS_ICONS_BUCKET_NAME);

            List<GridFSDBFile> pics = gfsPhoto.find(pictureId);
            if(pics.size()>0){
                byte[] picAsBytes = IOUtils.toByteArray(pics.get(0).getInputStream());
                ByteArrayResource resource = new ByteArrayResource(picAsBytes );
                return resource;
            }

        }
       return null;
    }

}
