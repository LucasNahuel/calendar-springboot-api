package com.lucas.calendarspringbootapi.Repositories;

import com.lucas.calendarspringbootapi.Models.Calendar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends CrudRepository<Calendar, Long> {
}
