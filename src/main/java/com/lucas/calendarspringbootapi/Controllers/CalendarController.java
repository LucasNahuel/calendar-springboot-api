package com.lucas.calendarspringbootapi.Controllers;


import com.lucas.calendarspringbootapi.Models.Calendar;
import com.lucas.calendarspringbootapi.Models.User;
import com.lucas.calendarspringbootapi.Repositories.CalendarRepository;
import com.lucas.calendarspringbootapi.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalendarController {
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    UserRepository userRepository;


    @GetMapping(path="/getCalendars/")
    public ResponseEntity<?> getCalendars(@RequestHeader("username") String username, @RequestHeader("password") String password){
        //get the user
        User user = userRepository.findByUsernameAndPassword(username, password);

        //get the calendars from the given user
        List<Calendar> calendars = calendarRepository.findAllByUserId(user);


        //map for the body of the response
        Map<Object, Object> paramsBody = new LinkedHashMap();
        paramsBody.put("value", calendars);


        //return the results
        return new ResponseEntity<>(paramsBody, HttpStatus.OK);
    }
}
