package com.refpro.server.controllers;

import com.refpro.server.DBhandlers.MatchInfoService;
import com.refpro.server.DBhandlers.UserHandler;
import com.refpro.server.DTOs.*;
import com.refpro.server.exception.MatchNotFoundException;
import com.refpro.server.models.MatchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestMapping("/matchInfo")
@Controller
public class MatchInfoController {
    @Autowired
    private MatchInfoService matchInfoHandler;
    private UserHandler userHandler = new UserHandler();
    private static final Logger log= Logger.getLogger(MatchInfoController.class.getName());

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public static ResponseEntity handleTypeMismatchException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseEntity handleNotReadableException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity createMatchInfo(@RequestBody NewMatchInfoDTO newMatchInfoDTO) throws Exception {
        String ID = matchInfoHandler.addMatchInfo(newMatchInfoDTO);
        log.log(Level.INFO,"Create matcg Info: "+newMatchInfoDTO);

        return  new ResponseEntity<>(ID,HttpStatus.OK);
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public HttpEntity getMatchInfo() {
        List<MatchInfo> result =matchInfoHandler.getAllMatchInfo();

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/addMatchEvent",method = RequestMethod.POST)
    public HttpEntity addMatchEvent(@PathVariable("id") String id, @RequestBody MatchEventDTO matchEventDTO) {
        try {
            matchInfoHandler.addMatchEventToMatch(id,matchEventDTO);
            System.out.println(matchEventDTO.toString());
        } catch (MatchNotFoundException e) {
            ErrorDto errorDto = new ErrorDto("Match not found");
            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/addPlayerEvent",method = RequestMethod.POST)
    public HttpEntity addPlayerEvent(@PathVariable("id") String id,@RequestBody PlayerEventDTO playerEventDTO) {
        try {
            matchInfoHandler.addPlayerEventToMatch(id,playerEventDTO);
        } catch (MatchNotFoundException e) {
            ErrorDto errorDto = new ErrorDto("Match not found");
            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/getMatchById",method = RequestMethod.GET)
    public HttpEntity getMatchById(@RequestParam("id") String id) {
        MatchInfoDTO result;

        try {
            result = matchInfoHandler.getMatchById(id);
        } catch (MatchNotFoundException e) {
            ErrorDto errorDto = new ErrorDto("Match not found");
            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public HttpEntity updateMatchInfo(@RequestBody MatchUpdateDTO matchUpdateDTO) {

        System.out.println(matchUpdateDTO.toString());
        try {
            matchInfoHandler.updateMatchInfo(matchUpdateDTO);
        } catch (MatchNotFoundException e) {
            ErrorDto errorDto = new ErrorDto("Match not found");
            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public HttpEntity deleteMatchInfo(@RequestParam("id") String id) {
        try {
            matchInfoHandler.deleteMatchInfo(id);
        } catch (MatchNotFoundException e) {
            ErrorDto errorDto = new ErrorDto("Match not found");
            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }
        log.log(Level.INFO,"Delete match: "+id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
