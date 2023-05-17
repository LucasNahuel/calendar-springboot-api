# React Calendar SpringBoot API

This is a spring boot based study project intended for provide back-end functionality to my [React calendar app](react-calendar-lemon.vercel.app) project.


## Table of contents

- [Overview](#Overview)
    - [Functionality](#functionality)
    - [Built with](#built-with)
- [Development](#development)
  - [Concepts Applied](#concepts-applied)
  - [What I've learned](#what-i've-learned)
- [Author](#author)

## Overview

### Functionality

Basic functionality was described as:

- Create calendars and events
- Calendars can have many events
- Being able to delete calendars and events
- Being able to edit events details
- Being able to visualize events over time span

### Built with

- [Spring Boot](https://spring.io/projects/spring-boot) - Java development environment
- Jakarta Persistance API
- [MySQL](https://www.mysql.com/) - Relational Database engine and DBMS
- [Maven](https://maven.apache.org/) - Project management tool


## Development

  ### Concepts Applied

  These are some core concepts applied to the development:

  - Object Oriented Programming
  - Relational Database management (MySQL)
  - RESTFULL
  - Inversion of Control (Spring Boot)
  - Java Date and Calendar objects usage
  - Java exceptions handling
  - JPA's entities and annotations to handle data models
  - Spring Boot repositories to handle database queries 
  - Spring Boot controller advisor to handle exceptions
  - Spring Boot controllers to handle endpoints

  ### What i've learned
  
  I've noticed that managing every possible error at every endpoint can become very cumbersome. Investigating over error handling at Spring Boot page, I've made acquaintance of "controller advisors" and the "ResponseEntityExceptionHandler" interface. Both of these can be used into a class implementing a series of "ExceptionHandler" annotated methods which can help to manage the errors along all the controllers in a more centralized, reusable way.
  [More about @ControllerAdvice](https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f)




## Author

- LinkedIn - [profile](https://www.linkedin.com/in/luki/)
- Website - [Lucas Munoz](https://angular-portfolio-lake.vercel.app/)
