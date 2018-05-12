package com.refpro.server.controllers;

import com.refpro.server.DBhandlers.RefereeHandler;
import com.refpro.server.DTOs.ErrorDto;
import com.refpro.server.DTOs.RefereeDTO;
import com.refpro.server.exception.AbstractRestException;
import com.refpro.server.exception.RefereeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/referee")
@Controller
public class RefereeController {

    @Autowired
    private RefereeHandler refereeHandler;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity getAllReferees() {
        return new ResponseEntity<>(refereeHandler.getAllReferees(),HttpStatus.OK);
    }

    @RequestMapping("/{refereeID}")
    public ResponseEntity getRefereeByID(@PathVariable String refereeID) {
        try {
            return new ResponseEntity<>(refereeHandler.getRefereeByID(refereeID),HttpStatus.OK);
        } catch (RefereeNotFoundException e) {
            ErrorDto errorDto = new ErrorDto(e.getMessage());
            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity createReferees(@RequestBody List<RefereeDTO> refereeDTOS) {
        List<String> result = new ArrayList<>();

        for (RefereeDTO refereeDTO : refereeDTOS) {
            result.add(refereeHandler.createReferee(refereeDTO));
        }

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{refereeID}/addPlayerIcon")
    public ResponseEntity addRefereePicture(@PathVariable String refereeID,
                                        @RequestParam(name="file", required=false) MultipartFile file) throws IOException, AbstractRestException {

        if(file!=null){
            refereeHandler.addRefereePicture(refereeID, file);
        }
        return new ResponseEntity<>("Uploaded", HttpStatus.OK);
    }

    @RequestMapping(path = "/{refereeID}/getRefereePicture", method = RequestMethod.GET)
    public ResponseEntity<Resource> getRefereePicture(@PathVariable String refereeID) throws IOException, AbstractRestException {

        ByteArrayResource responseResource = refereeHandler.getRefereePicture(refereeID);
        if(responseResource!=null) {
            return ResponseEntity.ok()
                    .contentLength(responseResource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(responseResource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{refereeID}/addMarkToReferee",method = RequestMethod.POST)
    public ResponseEntity addMarkToReferee(@PathVariable String refereeID, @RequestBody Double mark) {
        try {
            refereeHandler.addMarkToReferee(refereeID,mark);
        } catch (RefereeNotFoundException e) {
            ErrorDto errorDto = new ErrorDto(e.getMessage());

            return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
