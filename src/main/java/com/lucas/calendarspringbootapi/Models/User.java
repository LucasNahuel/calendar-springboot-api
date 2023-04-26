package com.lucas.calendarspringbootapi.Models;

import jakarta.persistence.*;


@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _id;

    @Column(unique = true)
    private String username;

    private String password;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}