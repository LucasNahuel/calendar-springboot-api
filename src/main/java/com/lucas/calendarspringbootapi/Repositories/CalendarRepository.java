package com.lucas.calendarspringbootapi.Repositories;

import com.lucas.calendarspringbootapi.Models.Calendar;
import com.lucas.calendarspringbootapi.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CalendarRepository extends CrudRepository<Calendar, Long> {

    public List<Calendar> findAllByUserId(User user);
}
