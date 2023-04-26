package com.lucas.calendarspringbootapi.Controllers;


import com.lucas.calendarspringbootapi.Models.Calendar;
import com.lucas.calendarspringbootapi.Repositories.CalendarRepository;
import com.lucas.calendarspringbootapi.exception.SavingError;
import com.lucas.calendarspringbootapi.exception.DuplicationError;
import com.lucas.calendarspringbootapi.Models.User;
import com.lucas.calendarspringbootapi.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @PostMapping(value = "/register", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody User u){

        String username = u.getUsername();
        String password = u.getPassword();



        if(userRepository.existsByUsername(username) ){
            throw new DuplicationError("Username");

        }else{
            try{

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);

                User savedUser = userRepository.save(newUser);

                //create a default calendar for the user

                Calendar defaultCalendar = new Calendar();
                defaultCalendar.setCalendarName("default calendar");
                defaultCalendar.setUserId(savedUser);

                calendarRepository.save(defaultCalendar);


                return new ResponseEntity<>("{ 'value' : 'Correctly saved'}", HttpStatus.OK);

            }catch (Error e){

                throw new SavingError("user");
            }
        }


    }

//    As this api is adapted to function with an existing frontend, i can't receive the parameters
//    as usually, instead reading from the actual url, as is done in express
    @GetMapping(path = "/usernameExists/{username}")
    public ResponseEntity<?> usernameExists(HttpServletRequest request){
        String[] urlParts = request.getRequestURL().toString().split("/");
        String username = urlParts[urlParts.length-1];

        boolean usernameExists = userRepository.existsByUsername(username);

        Map<Object, Object> body = new LinkedHashMap<>();

        body.put("value", usernameExists);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }



    @GetMapping(path = "/login/{username}/{password}")
    public ResponseEntity<?> login(HttpServletRequest request){

        String[] urlParts = request.getRequestURL().toString().split("/");
        String username = urlParts[urlParts.length-2];
        String password = urlParts[urlParts.length-1];

        User foundUser = userRepository.findByUsernameAndPassword(username, password);


        if(foundUser == null){
            return new ResponseEntity<>("{ \"valid\": \"false\"}", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("{ \"valid\": true ,\"username\" : \""+foundUser.getUsername()+"\", \"password\" : \""+foundUser.getPassword()+"\"}", HttpStatus.OK);

    }







}
