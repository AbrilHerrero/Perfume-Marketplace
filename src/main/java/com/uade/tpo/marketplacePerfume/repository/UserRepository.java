package com.uade.tpo.marketplacePerfume.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.uade.tpo.marketplacePerfume.entity.User;

public class UserRepository {
    private ArrayList<User> users;
    public UserRepository (){
        this.users=new ArrayList<User>(Arrays.asList(User.builder().id(0).name("Abril").email("abril123@uade.edu.ar").password("876543").telephone("44671256").registerDate(LocalDate.now()).active(true).build()));
    }    

    public User getUserByEmailAndPassword(String email, String password) {
        return this.users.stream()
            .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
            .findFirst()
            .orElse(null);
    }
    
    public ArrayList<User> getAllUsers(){
        return this.users;

    }

    public User getUserById(int id) {
        return this.users.stream()
            .filter(user -> user.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public User createUser(int id, String name, String email, String password, String telephone) {
        User user = User.builder().id(id).name(name).email(email).password(password).telephone(telephone).registerDate(LocalDate.now()).active(true).build();
        users.add(user);
        return user;
    }
    public void updatePassword(String email, String newPassword) {

        users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .ifPresent(user -> user.setPassword(newPassword));
    }

}
