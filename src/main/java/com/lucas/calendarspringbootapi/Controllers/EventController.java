package com.lucas.calendarspringbootapi.Controllers;


import java.util.*;

import com.lucas.calendarspringbootapi.Models.Event;
import com.lucas.calendarspringbootapi.Repositories.CalendarRepository;
import com.lucas.calendarspringbootapi.Repositories.EventRepository;
import com.lucas.calendarspringbootapi.exception.SavingError;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.criteria.CriteriaBuilder;
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


            //defines the end of the day from the received stamp
            cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DATE), 23, 59, 59);
            Date endDate = cal.getTime();


            //finds all events who begins this day
            List<Event> eventList = eventRepository.findAllByCalendarIdAndBeginDateGreaterThanEqualAndBeginDateLessThanEqual(optCalendar.get(), beginDate, endDate);
            //find all events who covers this day
            eventList.addAll(eventRepository.findAllByCalendarIdAndBeginDateLessThanAndEndDateGreaterThan(optCalendar.get(), beginDate, beginDate));

            List<Object> foundEventsEdited = new ArrayList<>();

            for(int i = 0 ; i < eventList.size(); i++){

                Event e = eventList.get(i);
                Map<Object, Object> eventMap = new LinkedHashMap<>();
                eventMap.put("_id", e.get_id());
                eventMap.put("name", e.getName());
                eventMap.put("description", e.getDescription());
                eventMap.put("beginDate", e.getBeginDate());
                eventMap.put("endDate", e.getEndDate());
                eventMap.put("calendarId", String.valueOf(e.getCalendarId().get_id()));
                foundEventsEdited.add(eventMap);
            }



            //map which contains the body for the response, including the list of events found
            Map<Object, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("value", foundEventsEdited);

            return new ResponseEntity<>(paramMap, HttpStatus.OK);
        }
        return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }


    @PostMapping(path = "/createEvent")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> body){


            Event event = new Event();
            event.setBeginDate(new Date((Long)body.get("beginDate")));
            event.setEndDate(new Date((Long)body.get("endDate")));
            event.setName((String)body.get("name"));
            event.setDescription((String)body.get("description"));

            Optional<com.lucas.calendarspringbootapi.Models.Calendar> cal = calendarRepository.findById(Long.valueOf((Integer)body.get("calendarId")));

            event.setCalendarId(cal.get());


            eventRepository.save(event);

            Map<Object, Object> bodyParams = new LinkedHashMap<>();

            bodyParams.put("value", "Event saved correctly");

            return new ResponseEntity<>(bodyParams, HttpStatus.OK);
    }

    @GetMapping(path="/getNextEvents/{calendarId}/{dateStamp}")
    public ResponseEntity<?> getNextEvents(HttpServletRequest request){
        String[] requestParams = request.getRequestURL().toString().split("/");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(requestParams[requestParams.length-1]));
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 00, 00, 00);


        Date dateBegin = cal.getTime();
        Date limit = new Date(dateBegin.getTime()+(1000*60*60*24*7));
        Optional<com.lucas.calendarspringbootapi.Models.Calendar> calendar = calendarRepository.findById(Long.valueOf(requestParams[requestParams.length-2]));

        List<Event> foundEvents = eventRepository.findAllByCalendarIdAndBeginDateGreaterThanEqualAndBeginDateLessThanEqual(calendar.get(), dateBegin, limit);

        if(foundEvents.size()>10){
            foundEvents = foundEvents.subList(0,10);
        }

        //I can't just return event elements, as the other api don't send calendars objects directly, just the id of them

        List<Object> foundEventsEdited = new ArrayList<>();

        for(int i = 0 ; i < foundEvents.size(); i++){

            Event e = foundEvents.get(i);
            Map<Object, Object> eventMap = new LinkedHashMap<>();
            eventMap.put("_id", e.get_id());
            eventMap.put("name", e.getName());
            eventMap.put("description", e.getDescription());
            eventMap.put("beginDate", e.getBeginDate());
            eventMap.put("endDate", e.getEndDate());
            eventMap.put("calendarId", String.valueOf(e.getCalendarId().get_id()));
            foundEventsEdited.add(eventMap);
        }



        Map<Object, Object> responseParams = new LinkedHashMap<>();
        responseParams.put("value", foundEventsEdited);

        return new ResponseEntity<>(responseParams, HttpStatus.OK);
    }

    @RequestMapping(path="/editEvent", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> editEvent(@RequestBody Object body){


        //cast requestbody object to a map
        Map<String, Object> bodyMap = (LinkedHashMap<String, Object>)body;


        Optional<Event> optEv = eventRepository.findById(Long.valueOf((Integer)bodyMap.get("_id")));
        Event ev = optEv.get();
        ev.setName((String) bodyMap.get("name"));
        ev.setDescription((String)bodyMap.get("description"));
        ev.setBeginDate(new Date((Long)bodyMap.get("beginDate")));
        ev.setEndDate(new Date((Long)bodyMap.get("endDate")));

        ev.setCalendarId(calendarRepository.findById(Long.valueOf((String)bodyMap.get("calendarId"))).get());

        eventRepository.save(ev);

        Map<Object, Object> responseParams = new LinkedHashMap<>();
        responseParams.put("value", "Correctly edited");

        return new ResponseEntity<>(responseParams, HttpStatus.OK);
    }
}
