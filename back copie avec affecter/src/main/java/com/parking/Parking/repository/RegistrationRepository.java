package com.parking.Parking.repository;

import com.parking.Parking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<User,Integer> {
    public User findByLogin(String login);
    public User findByLoginAndPwd(String login,String pwd);
}
