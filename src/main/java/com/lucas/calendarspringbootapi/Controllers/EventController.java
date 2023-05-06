package com.lucas.calendarspringbootapi.Controllers;


import java.util.*;

import com.lucas.calendarspringbootapi.Models.Event;
import com.lucas.calendarspringbootapi.Repositories.CalendarRepository;
import com.lucas.calendarspringbootapi.Repositories.EventRepository;
import com.lucas.calendarspringbootapi.exception.SavingError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    CalendarRepository calendarRepository;

    @GetMapping(path= "/getEventsByDay/{calendarId}/{dayToFindStamp}")
    public ResponseEntity<?> getEventsByDay(HttpServletRequest request){




        String[] urlParts = request.getRequestURL().toString().split("/");
        String dayToFindStamp = urlParts[urlParts.length-1];
        String calendarId = urlParts[urlParts.length-2];

        System.out.println("get events by day called "+"daytofindstap "+dayToFindStamp+"calendarid "+calendarId);

        //find the calendar from the received id
        if(Long.valueOf(calendarId) != null) {
            Optional<com.lucas.calendarspringbootapi.Models.Calendar> optCalendar = calendarRepository.findById(Long.valueOf(calendarId));


            //set a calendar with the received epoch stamp
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(dayToFindStamp));
            Date receivedDate = cal.getTime();
            //defines the beginning of the day from the received epoch stamp
            cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DATE), 00, 00, 00);
            Date beginDate = cal.getTime();

            System.out.println("begin date : " + cal.get(cal.YEAR)+" "+(cal.get(cal.MONTH))+" "+cal.get(cal.DATE));
            System.out.println("begin date stamp : "+cal.getTimeInMillis());

            //defines the end of the day from the received stamp
            cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DATE), 23, 59, 59);
            Date endDate = cal.getTime();

            System.out.println("end date : " + cal.get(cal.YEAR)+" "+(cal.get(cal.MONTH))+" "+cal.get(cal.DATE));
            System.out.println("end date stamp : "+cal.getTimeInMillis());


            List<Event> eventList = eventRepository.findAllByCalendarIdAndBeginDateGreaterThanEqualAndBeginDateLessThanEqual(optCalendar.get(), beginDate, endDate);

            System.out.println("found elements: " + eventList.size());
            //map which contains the body for the response, including the list of events found
            Map<Object, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("value", eventList);

            return new ResponseEntity<>(paramMap, HttpStatus.OK);
        }
        return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }


    @PostMapping(path = "/createEvent")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> body){

            System.out.println(body.get("name"));

            Event event = new Event();
            event.setBeginDate(new Date((Long)body.get("beginDate")));
            event.setEndDate(new Date((Long)body.get("endDate")));
            event.setName((String)body.get("name"));
            event.setDescription((String)body.get("description"));

            Optional<com.lucas.calendarspringbootapi.Models.Calendar> cal = calendarRepository.findById(Long.valueOf((Integer)body.get("calendarId")));

            event.setCalendarId(cal.get());

            System.out.println("saving calendar" +event.getName());

            eventRepository.save(event);

            Map<Object, Object> bodyParams = new LinkedHashMap<>();

            bodyParams.put("value", "Event saved correctly");

            return new ResponseEntity<>(bodyParams, HttpStatus.OK);
    }
}
