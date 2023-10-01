package com.parking.Parking.controller;

import com.parking.Parking.entities.User;
import com.parking.Parking.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService service;
    @PostMapping("/registeruser")

    public User registerUser(@RequestBody User user) throws Exception {
        String login =user.getLogin();
        if(login!=null && !"".equals(login)) {
           User userObj = service.fetchUserByLogin(login);
           if (userObj != null ){
               throw new Exception("Login Unavailable");
           }
        }
        User userObj=null;
        userObj = service.saveUser(user);
        return userObj;
    }
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")

    public User loginUser(@RequestBody User user) throws Exception {
        String tempLogin = user.getLogin();
        String tempPwd = user.getPwd();
        User userObj = null;
        if(tempLogin != null && tempPwd !=null ){
           userObj = service.fetchUserByLoginAndPwd(tempLogin,tempPwd);
        }
        if(userObj==null){
            throw new Exception("Account doesnt exist");
        }
        return userObj;

    }

}
