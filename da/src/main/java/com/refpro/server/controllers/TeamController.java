package com.refpro.server.controllers;


import com.refpro.server.DBhandlers.TeamHandler;
import com.refpro.server.DTOs.ErrorDto;
import com.refpro.server.DTOs.NewMatchInfoDTO;
import com.refpro.server.DTOs.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.logging.Level;

@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamHandler teamHandler;


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public static ResponseEntity handleTypeMismatchException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseEntity handleNotReadableException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity createNewTeam(@RequestBody List<TeamDto> teamsDto) {

       // log.log(Level.INFO,"Create matcg Info: "+newMatchInfoDTO);
        List<String> ids=teamHandler.createNewTeam(teamsDto);
        return  new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public ResponseEntity getAllTeams() {

        // log.log(Level.INFO,"Create matcg Info: "+newMatchInfoDTO);
        List<TeamDto> result = teamHandler.getAllTeams();
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }

}
