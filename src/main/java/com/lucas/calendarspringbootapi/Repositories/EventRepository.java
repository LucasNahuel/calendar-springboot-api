package com.lucas.calendarspringbootapi.Repositories;

import com.lucas.calendarspringbootapi.Models.Calendar;
import com.lucas.calendarspringbootapi.Models.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    public List<Event> findAllByCalendarIdAndBeginDateGreaterThanEqualAndBeginDateLessThanEqual(Calendar calendarId, Date beginDate, Date endDate);


}
