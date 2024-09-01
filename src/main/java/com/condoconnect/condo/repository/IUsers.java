package com.condoconnect.condo.repository;

import com.condoconnect.condo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsers extends JpaRepository<User, Integer> {

   public User findByNameOrEmail(String name, String email);
}
