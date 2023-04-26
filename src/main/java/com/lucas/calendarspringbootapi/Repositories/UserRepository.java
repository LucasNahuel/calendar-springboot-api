package com.lucas.calendarspringbootapi.Repositories;

import com.lucas.calendarspringbootapi.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    public boolean existsByUsername(String username);

    public User findByUsernameAndPassword(String username, String password);
}
