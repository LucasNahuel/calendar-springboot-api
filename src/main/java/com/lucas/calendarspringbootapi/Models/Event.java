package com.lucas.calendarspringbootapi.Models;


import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _id;

    private String name;
    private Date beginDate;
    private Date endDate;
    private String description;

    @ManyToOne
    private Calendar calendarId;
}
