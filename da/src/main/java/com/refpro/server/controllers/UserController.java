package com.refpro.server.controllers;

import com.refpro.server.DBhandlers.FirebaseTopicsHandler;
import com.refpro.server.DBhandlers.UserHandler;
import com.refpro.server.DTOs.ErrorDto;
import com.refpro.server.DTOs.FirebaseTopicDTO;
import com.refpro.server.DTOs.UserDTO;
import com.refpro.server.enums.DeviceType;
import com.refpro.server.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserHandler userHandler;

    @Autowired
    private FirebaseTopicsHandler firebaseTopicsHandler;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public static ResponseEntity handleTypeMismatchException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseEntity handleNotReadableException() {
        return new ResponseEntity<>(new ErrorDto("Invalid input"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateUserToken",method = RequestMethod.POST)
    public HttpEntity saveToken(@RequestBody String fcmTokenForUser,@RequestHeader("DeviceType") String deviceTypeHeader) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        DeviceType deviceType = DeviceType.valueOf(deviceTypeHeader);
        userHandler.updateUserFcmToken(userName,fcmTokenForUser.replace("\"",""),deviceType);
        System.out.println("asd");
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public HttpEntity register(@RequestBody UserDTO userDTO) throws Exception {
        userHandler.registerNewUserAccount(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/getTokensForUser",method = RequestMethod.GET)
    public HttpEntity getTokenForUser() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        UserDTO userDto = userHandler.getFCMtokenForUser(userName);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
    @RequestMapping(value = "/getTopic",method = RequestMethod.GET)
    public  HttpEntity getTopicsForUser() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        List<FirebaseTopicDTO> topicsForUser = firebaseTopicsHandler.getTopicsForUsername(userName);
        return new ResponseEntity<>(topicsForUser,HttpStatus.OK);
    }

}
