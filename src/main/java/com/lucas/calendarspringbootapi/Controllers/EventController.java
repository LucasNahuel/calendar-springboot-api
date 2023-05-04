package com.lucas.calendarspringbootapi.Controllers;


import java.util.*;

import com.lucas.calendarspringbootapi.Models.Event;
import com.lucas.calendarspringbootapi.Repositories.CalendarRepository;
import com.lucas.calendarspringbootapi.Repositories.EventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    CalendarRepository calendarRepository;

    @GetMapping(path= "/getEventsByDay/{calendarId}/{dayToFindStamp}/")
    public ResponseEntity<?> getEventsByDay(HttpServletRequest request){

        String[] urlParts = request.getRequestURL().toString().split("/");
        String dayToFindStamp = urlParts[urlParts.length-1];
        String calendarId = urlParts[urlParts.length-2];

        //find the calendar from the received id
        Optional<com.lucas.calendarspringbootapi.Models.Calendar> optCalendar = calendarRepository.findById(Long.getLong(calendarId));

        //set a calendar with the received epoch stamp
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.getLong(dayToFindStamp));
        Date receivedDate = cal.getTime();
        //defines the beginning of the day from the received epoch stamp
        cal.set(cal.YEAR, cal.MONTH, cal.DATE, 00, 00, 00);
        Date beginDate = cal.getTime();
        //defines the end of the day from the received stamp
        cal.set(cal.YEAR, cal.MONTH, cal.DATE, 23, 59, 59);
        Date endDate = cal.getTime();

        List<Event> eventList = eventRepository.findAllByCalendarIdAndBeginDateGreaterThanEqualAndBeginDateLessThanEqual(optCalendar.get(), beginDate, endDate);

        //map which contains the body for the response, including the list of events found
        Map<Object, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("value", eventList);

        return new ResponseEntity<>(paramMap, HttpStatus.OK);

    }
}
