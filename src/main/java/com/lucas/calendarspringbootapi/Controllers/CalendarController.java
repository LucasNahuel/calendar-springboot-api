package com.lucas.calendarspringbootapi.Controllers;


import com.lucas.calendarspringbootapi.Models.Calendar;
import com.lucas.calendarspringbootapi.Models.User;
import com.lucas.calendarspringbootapi.Repositories.CalendarRepository;
import com.lucas.calendarspringbootapi.Repositories.UserRepository;
import com.lucas.calendarspringbootapi.exception.SavingError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/createCalendar")
    public ResponseEntity<?> createCalendar(@RequestBody Map<String, String> bodyParams, @RequestHeader Map<String, String> headerParams){


        try{
            String calendarName = bodyParams.get("calendarName");
            String calendarDescription = bodyParams.get("description");
            User calendarOwner = userRepository.findByUsernameAndPassword(headerParams.get("username"), headerParams.get("password"));

            Calendar calendar = new Calendar();
            calendar.setCalendarName(calendarName);
            calendar.setDescription(calendarDescription);
            calendar.setUserId(calendarOwner);
            calendarRepository.save(calendar);

            Map<Object, Object> responseParams = new LinkedHashMap<>();
            responseParams.put("value", "Correctly saved");

            return new ResponseEntity<>(responseParams, HttpStatus.OK);

        }catch(Error e){
            throw new SavingError("calendar");
        }
    }

    @DeleteMapping(path= "/deleteCalendar/{calendarId}")
    public ResponseEntity<?> deleteCalendar(HttpServletRequest request) {

        //get the calendar id from the url route
        String[] requestArray = request.getRequestURL().toString().split("/");
        Long calendarId = Long.valueOf(requestArray[requestArray.length - 1]);

        //delete calendar by id
        calendarRepository.deleteById(calendarId);

        //map for body response parameters
        Map<Object, Object> responseParams = new LinkedHashMap<>();
        responseParams.put("value", "Correctly deleted");

        return new ResponseEntity<>(responseParams, HttpStatus.OK);
    }
}
