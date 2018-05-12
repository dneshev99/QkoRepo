package com.refpro.server.controllers;


import com.refpro.server.DBhandlers.PlayerService;
import com.refpro.server.DTOs.ErrorDto;
import com.refpro.server.DTOs.PlayerDTO;
import com.refpro.server.exception.AbstractRestException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerSericeHandler;

    private static final Logger log = Logger.getLogger(PlayerController.class.getName());

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public static ResponseEntity handleTypeMismatchException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseEntity handleNotReadableException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity createNewPlayers(@RequestBody List<PlayerDTO> playersToCreate) throws AbstractRestException {

        log.log(Level.INFO,"createNewPlayers: "+playersToCreate);
        List<String> ids=playerSericeHandler.createPlayer(playersToCreate);
        return  new ResponseEntity<>( ids,HttpStatus.OK);
    }

    @RequestMapping(value = "/getPlayersByTeam/{name}",method = RequestMethod.GET)
    public ResponseEntity getPlayersByTeam(@PathVariable("name") String name) throws Exception {
        List<PlayerDTO> result = playerSericeHandler. getPlayersByTeam(name);
        log.debug("getPlayersByTeam "+name);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

   // @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "{playerId}/addPlayerIcon")
    public ResponseEntity addPlayerIcon(@PathVariable String playerId,
                                              @RequestParam(name="file", required=false) MultipartFile file) throws IOException, AbstractRestException {

        if(file!=null){
            playerSericeHandler.addPlayerIcon(playerId, file);
        }
        return new ResponseEntity<>("Uploaded",HttpStatus.OK);
    }

    @RequestMapping(path = "/{playerId}/getPlayerIcon", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPlayerIcon(@PathVariable String playerId) throws IOException, AbstractRestException {

        ByteArrayResource responseResource = playerSericeHandler.getPlayerIcon(playerId);
        if(responseResource!=null) {
            return ResponseEntity.ok()
                    .contentLength(responseResource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(responseResource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
