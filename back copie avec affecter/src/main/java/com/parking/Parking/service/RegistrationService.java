package com.parking.Parking.service;

import com.parking.Parking.entities.User;
import com.parking.Parking.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository repo;
    public User saveUser(User user){
        return repo.save(user);

    }
    public User fetchUserByLogin(String login){
        return repo.findByLogin(login);
    }

    public User fetchUserByLoginAndPwd(String login, String pwd){
        return repo.findByLoginAndPwd(login,pwd);
    }
}
